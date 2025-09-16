package br.com.fiap.hackaton.dto;

import br.com.fiap.hackaton.enums.StatusAgendamento;

import java.time.LocalDateTime;

public class CancelamentoResposta {
    private String agendamentoId;
    private String pacienteId;
    private LocalDateTime dataHora;
    private StatusAgendamento status;
    private String mensagem;

    public CancelamentoResposta() {
    }

    public CancelamentoResposta(String agendamentoId, String pacienteId, LocalDateTime dataHora, StatusAgendamento status, String mensagem) {
        this.agendamentoId = agendamentoId;
        this.pacienteId = pacienteId;
        this.dataHora = dataHora;
        this.status = status;
        this.mensagem = mensagem;
    }

    public String getAgendamentoId() {
        return agendamentoId;
    }

    public void setAgendamentoId(String agendamentoId) {
        this.agendamentoId = agendamentoId;
    }

    public String getPacienteId() {
        return pacienteId;
    }

    public void setPacienteId(String pacienteId) {
        this.pacienteId = pacienteId;
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

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }
}
