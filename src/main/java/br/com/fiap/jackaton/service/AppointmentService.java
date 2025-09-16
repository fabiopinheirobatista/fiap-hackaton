package br.com.fiap.jackaton.service;

import br.com.fiap.jackaton.dto.AppointmentRequest;
import br.com.fiap.jackaton.dto.SuggestionResponse;
import br.com.fiap.jackaton.entity.AppointmentEntity;
import br.com.fiap.jackaton.entity.PatientEntity;
import br.com.fiap.jackaton.entity.SlotEntity;
import br.com.fiap.jackaton.entity.UnitEntity;
import br.com.fiap.jackaton.enums.AppointmentStatus;
import br.com.fiap.jackaton.enums.AppointmentType;
import br.com.fiap.jackaton.model.Appointment;
import br.com.fiap.jackaton.repository.AppointmentRepository;
import br.com.fiap.jackaton.repository.PatientRepository;
import br.com.fiap.jackaton.repository.SlotRepository;
import br.com.fiap.jackaton.repository.UnitRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AppointmentService {

    private final PatientRepository patientRepository;
    private final UnitRepository unitRepository;
    private final AppointmentRepository appointmentRepository;
    private final SlotRepository slotRepository;

    public AppointmentService(PatientRepository patientRepository, UnitRepository unitRepository, AppointmentRepository appointmentRepository, SlotRepository slotRepository) {
        this.patientRepository = patientRepository;
        this.unitRepository = unitRepository;
        this.appointmentRepository = appointmentRepository;
        this.slotRepository = slotRepository;
    }

    public SuggestionResponse suggest(AppointmentRequest request) {
        PatientEntity patient = patientRepository.findById(request.getPatientId()).orElseThrow(() -> new IllegalArgumentException("Paciente não encontrado"));
        if (!patient.isActive()) throw new IllegalArgumentException("Paciente inativo");

        AppointmentType type = request.getType();
        String location = request.getLocation();
        int urgency = Math.max(1, Math.min(5, request.getUrgency()));

        List<UnitEntity> units = unitRepository.findByTypeAndLocation(type, location);
        if (units.isEmpty()) {
            units = unitRepository.findByTypeAndLocation(type, null); // fallback: ignore location
        }
        if (units.isEmpty()) throw new IllegalStateException("Nenhuma unidade disponível para o tipo solicitado na localização informada");

        Optional<SuggestionResponse> best = units.stream()
                .map(unit -> unit.getAvailableSlots().stream()
                        .min(Comparator.comparing(SlotEntity::getDateTime))
                        .map(slot -> new UnitSlot(unit, slot)))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .min(Comparator.comparingDouble(us -> score(us.slot.getDateTime(), request.getLocation(), us.unit.getLocation(), urgency)))
                .map(us -> new SuggestionResponse(us.unit.getId(), us.unit.getName(), us.slot.getDateTime(), us.slot.getProfessionalId()));

        return best.orElseThrow(() -> new IllegalStateException("Nenhum horário disponível"));
    }

    @Transactional
    public Appointment confirm(String patientId, String unitId, LocalDateTime dateTime, String professionalId, AppointmentType type) {
        PatientEntity patient = patientRepository.findById(requestPatientId(patientId)).orElseThrow(() -> new IllegalArgumentException("Paciente não encontrado"));
        if (!patient.isActive()) throw new IllegalArgumentException("Paciente inativo");

        if (appointmentRepository.existsByPatientIdAndDateTimeAndStatusNot(patientId, dateTime, AppointmentStatus.CANCELADA)) {
            throw new IllegalStateException("Conflito de horário para o paciente");
        }

        int deleted = slotRepository.deleteByUnitIdAndDateTimeAndProfessionalId(unitId, dateTime, professionalId);
        if (deleted == 0) {
            throw new IllegalStateException("Horário não disponível");
        }

        AppointmentEntity entity = new AppointmentEntity(UUID.randomUUID().toString(), patientId, unitId, professionalId, type, dateTime, AppointmentStatus.AGENDADA);
        AppointmentEntity saved = appointmentRepository.save(entity);

        return new Appointment(saved.getId(), saved.getPatientId(), saved.getUnitId(), saved.getProfessionalId(), saved.getType(), saved.getDateTime(), saved.getStatus());
    }

    private String requestPatientId(String pid) { return pid; }

    private static double score(LocalDateTime slotDate, String requestedLocation, String unitLocation, int urgency) {
        long days = Duration.between(LocalDateTime.now(), slotDate).toDays();
        double score = days;
        if (requestedLocation != null && !requestedLocation.isBlank() && requestedLocation.equalsIgnoreCase(unitLocation)) {
            score -= 2.0;
        }
        score -= (urgency - 1) * 0.5;
        return score;
    }

    private static class UnitSlot {
        final UnitEntity unit;
        final SlotEntity slot;

        UnitSlot(UnitEntity unit, SlotEntity slot) {
            this.unit = unit;
            this.slot = slot;
        }
    }
}
