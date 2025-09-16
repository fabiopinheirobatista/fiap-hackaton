package br.com.fiap.jackaton.model;

import java.time.LocalDateTime;

public class Slot {
    private final LocalDateTime dateTime;
    private final String professionalId;

    public Slot(LocalDateTime dateTime, String professionalId) {
        this.dateTime = dateTime;
        this.professionalId = professionalId;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getProfessionalId() {
        return professionalId;
    }
}

