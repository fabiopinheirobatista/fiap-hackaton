package br.com.fiap.jackaton.dto;

import br.com.fiap.jackaton.enums.AppointmentType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class AppointmentRequest {
    @NotBlank(message = "patientId é obrigatório")
    private String patientId;

    @NotNull(message = "type é obrigatório")
    private AppointmentType type;

    @Size(max = 500, message = "symptoms deve ter no máximo 500 caracteres")
    private String symptoms;

    @NotBlank(message = "location é obrigatório")
    private String location;

    @Min(value = 1, message = "urgency deve ser entre 1 e 5")
    @Max(value = 5, message = "urgency deve ser entre 1 e 5")
    private int urgency = 1;

    public AppointmentRequest() {}

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public AppointmentType getType() {
        return type;
    }

    public void setType(AppointmentType type) {
        this.type = type;
    }

    public String getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(String symptoms) {
        this.symptoms = symptoms;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getUrgency() {
        return urgency;
    }

    public void setUrgency(int urgency) {
        this.urgency = urgency;
    }
}
