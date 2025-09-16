package br.com.fiap.jackaton.dto;

import java.time.LocalDateTime;

public class SuggestionResponse {
    private String unitId;
    private String unitName;
    private LocalDateTime dateTime;
    private String professionalId;

    public SuggestionResponse() {}

    public SuggestionResponse(String unitId, String unitName, LocalDateTime dateTime, String professionalId) {
        this.unitId = unitId;
        this.unitName = unitName;
        this.dateTime = dateTime;
        this.professionalId = professionalId;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
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
}

