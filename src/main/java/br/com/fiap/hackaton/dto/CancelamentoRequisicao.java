package br.com.fiap.hackaton.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CancelamentoRequisicao {

    @NotBlank(message = "ID do agendamento é obrigatório")
    @Size(max = 50, message = "ID do agendamento deve ter no máximo 50 caracteres")
    private String agendamentoId;

    @NotBlank(message = "ID do paciente é obrigatório")
    @Size(max = 50, message = "ID do paciente deve ter no máximo 50 caracteres")
    private String pacienteId;

    @Size(max = 255, message = "Motivo deve ter no máximo 255 caracteres")
    private String motivo;

    public CancelamentoRequisicao() {
    }

    public CancelamentoRequisicao(String agendamentoId, String pacienteId, String motivo) {
        this.agendamentoId = agendamentoId;
        this.pacienteId = pacienteId;
        this.motivo = motivo;
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

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }
}
