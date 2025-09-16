package br.com.fiap.jackaton.model;

import br.com.fiap.jackaton.enums.AppointmentStatus;
import br.com.fiap.jackaton.enums.AppointmentType;

import java.time.LocalDateTime;
import java.util.Objects;

public class Appointment {
    private final String id;
    private final String patientId;
    private final String unitId;
    private final String professionalId;
    private final AppointmentType type;
    private final LocalDateTime dateTime;
    private AppointmentStatus status;

    public Appointment(String id, String patientId, String unitId, String professionalId, AppointmentType type, LocalDateTime dateTime, AppointmentStatus status) {
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

    public String getPatientId() {
        return patientId;
    }

    public String getUnitId() {
        return unitId;
    }

    public String getProfessionalId() {
        return professionalId;
    }

    public AppointmentType getType() {
        return type;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public AppointmentStatus getStatus() {
        return status;
    }

    public void setStatus(AppointmentStatus status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Appointment that = (Appointment) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

