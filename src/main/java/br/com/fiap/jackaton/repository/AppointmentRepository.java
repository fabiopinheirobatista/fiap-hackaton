package br.com.fiap.jackaton.repository;

import br.com.fiap.jackaton.entity.AppointmentEntity;
import br.com.fiap.jackaton.enums.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface AppointmentRepository extends JpaRepository<AppointmentEntity, String> {
    boolean existsByPatientIdAndDateTimeAndStatusNot(String patientId, LocalDateTime dateTime, AppointmentStatus status);
}
