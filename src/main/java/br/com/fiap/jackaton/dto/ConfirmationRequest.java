package br.com.fiap.jackaton.dto;

import br.com.fiap.jackaton.enums.AppointmentType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class ConfirmationRequest {
    @NotBlank(message = "ID do Paciente é obrigatório")
    private String patientId;

    @NotBlank(message = "ID da Unidade é obrigatório")
    private String unitId;

    @NotNull(message = "Data e Hora são obrigatórios")
    private LocalDateTime dateTime;

    @NotBlank(message = "ID do Profissional é obrigatório")
    private String professionalId;

    @NotNull(message = "Tipo é obrigatório")
    private AppointmentType type;

    public ConfirmationRequest() {}

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

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
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
}
