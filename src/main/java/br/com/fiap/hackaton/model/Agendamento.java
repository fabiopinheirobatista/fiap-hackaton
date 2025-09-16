package br.com.fiap.hackaton.model;

import br.com.fiap.hackaton.enums.StatusAgendamento;
import br.com.fiap.hackaton.enums.TipoAgendamento;

import java.time.LocalDateTime;

public class Agendamento {
    private String id;
    private String pacienteId;
    private String unidadeId;
    private String profissionalId;
    private TipoAgendamento tipo;
    private LocalDateTime dataHora;
    private StatusAgendamento status;

    public Agendamento() {}

    public Agendamento(String id, String pacienteId, String unidadeId, String profissionalId, TipoAgendamento tipo, LocalDateTime dataHora, StatusAgendamento status) {
        this.id = id;
        this.pacienteId = pacienteId;
        this.unidadeId = unidadeId;
        this.profissionalId = profissionalId;
        this.tipo = tipo;
        this.dataHora = dataHora;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPacienteId() {
        return pacienteId;
    }

    public void setPacienteId(String pacienteId) {
        this.pacienteId = pacienteId;
    }

    public String getUnidadeId() {
        return unidadeId;
    }

    public void setUnidadeId(String unidadeId) {
        this.unidadeId = unidadeId;
    }

    public String getProfissionalId() {
        return profissionalId;
    }

    public void setProfissionalId(String profissionalId) {
        this.profissionalId = profissionalId;
    }

    public TipoAgendamento getTipo() {
        return tipo;
    }

    public void setTipo(TipoAgendamento tipo) {
        this.tipo = tipo;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public StatusAgendamento getStatus() {
        return status;
    }

    public void setStatus(StatusAgendamento status) {
        this.status = status;
    }
}
