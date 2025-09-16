package br.com.fiap.jackaton.entity;

import br.com.fiap.jackaton.enums.AppointmentStatus;
import br.com.fiap.jackaton.enums.AppointmentType;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "appointments")
public class AppointmentEntity {

    @Id
    @Column(length = 50)
    private String id;

    @Column(length = 50, nullable = false)
    private String patientId;

    @Column(length = 50, nullable = false)
    private String unitId;

    @Column(length = 50)
    private String professionalId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AppointmentType type;

    @Column(nullable = false)
    private LocalDateTime dateTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AppointmentStatus status;

    public AppointmentEntity() {}

    public AppointmentEntity(String id, String patientId, String unitId, String professionalId, AppointmentType type, LocalDateTime dateTime, AppointmentStatus status) {
        this.id = id;
        this.patientId = patientId;
        this.unitId = unitId;
        this.professionalId = professionalId;
        this.type = type;
        this.dateTime = dateTime;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public String getProfessionalId() {
        return professionalId;
    }

    public void setProfessionalId(String professionalId) {
        this.professionalId = professionalId;
    }

    public AppointmentType getType() {
        return type;
    }

    public void setType(AppointmentType type) {
        this.type = type;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public AppointmentStatus getStatus() {
        return status;
    }

    public void setStatus(AppointmentStatus status) {
        this.status = status;
    }
}

