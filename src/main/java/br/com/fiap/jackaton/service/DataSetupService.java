package br.com.fiap.jackaton.service;

import br.com.fiap.jackaton.entity.PatientEntity;
import br.com.fiap.jackaton.entity.SlotEntity;
import br.com.fiap.jackaton.entity.UnitEntity;
import br.com.fiap.jackaton.enums.AppointmentType;
import br.com.fiap.jackaton.repository.AppointmentRepository;
import br.com.fiap.jackaton.repository.PatientRepository;
import br.com.fiap.jackaton.repository.SlotRepository;
import br.com.fiap.jackaton.repository.UnitRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.ArrayList;

@Service
public class DataSetupService {

    private final PatientRepository patientRepository;
    private final UnitRepository unitRepository;
    private final SlotRepository slotRepository;
    private final AppointmentRepository appointmentRepository;

    public DataSetupService(PatientRepository patientRepository, UnitRepository unitRepository, SlotRepository slotRepository, AppointmentRepository appointmentRepository) {
        this.patientRepository = patientRepository;
        this.unitRepository = unitRepository;
        this.slotRepository = slotRepository;
        this.appointmentRepository = appointmentRepository;
    }

    @Transactional
    public void resetData() {
        // clear appointments and slots to ensure tests are idempotent
        appointmentRepository.deleteAll();
        slotRepository.deleteAll();

        // ensure patients
        if (!patientRepository.existsById("p1")) {
            patientRepository.save(new PatientEntity("p1", "Maria Silva", true));
        } else {
            PatientEntity p1 = patientRepository.findById("p1").orElseThrow();
            p1.setActive(true);
            p1.setName("Maria Silva");
            patientRepository.save(p1);
        }
        if (!patientRepository.existsById("p2")) {
            patientRepository.save(new PatientEntity("p2", "João Souza", true));
        } else {
            PatientEntity p2 = patientRepository.findById("p2").orElseThrow();
            p2.setActive(true);
            p2.setName("João Souza");
            patientRepository.save(p2);
        }
        if (!patientRepository.existsById("p3")) {
            patientRepository.save(new PatientEntity("p3", "Inativo", false));
        } else {
            PatientEntity p3 = patientRepository.findById("p3").orElseThrow();
            p3.setActive(false);
            p3.setName("Inativo");
            patientRepository.save(p3);
        }

        // recreate units and slots
        UnitEntity u1 = unitRepository.findById("u1").orElse(new UnitEntity("u1", "UBS Centro", "Centro"));
        u1.setName("UBS Centro");
        u1.setLocation("Centro");
        u1.setSupportedTypes(new ArrayList<>(Arrays.asList(AppointmentType.CLINICO_GERAL, AppointmentType.CARDIOLOGIA)));
        u1.getAvailableSlots().clear();
        u1.addSlot(new SlotEntity(LocalDateTime.now().plusDays(1).withHour(9).withMinute(0).withSecond(0).withNano(0), "prof1"));
        u1.addSlot(new SlotEntity(LocalDateTime.now().plusDays(1).withHour(10).withMinute(0).withSecond(0).withNano(0), "prof1"));
        unitRepository.save(u1);

        UnitEntity u2 = unitRepository.findById("u2").orElse(new UnitEntity("u2", "UBS Bairro", "Bairro"));
        u2.setName("UBS Bairro");
        u2.setLocation("Bairro");
        u2.setSupportedTypes(new ArrayList<>(Arrays.asList(AppointmentType.CLINICO_GERAL, AppointmentType.PEDIATRIA)));
        u2.getAvailableSlots().clear();
        u2.addSlot(new SlotEntity(LocalDateTime.now().plusDays(1).withHour(11).withMinute(0).withSecond(0).withNano(0), "prof2"));
        u2.addSlot(new SlotEntity(LocalDateTime.now().plusDays(2).withHour(9).withMinute(0).withSecond(0).withNano(0), "prof2"));
        unitRepository.save(u2);
    }
}
