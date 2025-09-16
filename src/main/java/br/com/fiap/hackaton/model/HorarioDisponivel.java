package br.com.fiap.hackaton.model;

import java.time.LocalDateTime;

public class HorarioDisponivel {
    private final LocalDateTime dataHora;
    private final String idProfissional;

    public HorarioDisponivel(LocalDateTime dataHora, String idProfissional) {
        this.dataHora = dataHora;
        this.idProfissional = idProfissional;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public String getIdProfissional() {
        return idProfissional;
    }
}
