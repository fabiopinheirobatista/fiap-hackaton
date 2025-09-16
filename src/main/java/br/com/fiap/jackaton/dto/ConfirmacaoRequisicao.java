package br.com.fiap.jackaton.dto;

import br.com.fiap.jackaton.enums.TipoAgendamento;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public class ConfirmacaoRequisicao {
    @NotBlank(message = "pacienteId é obrigatório")
    @Size(max = 50, message = "pacienteId deve ter no máximo 50 caracteres")
    private String pacienteId;

    @NotBlank(message = "unidadeId é obrigatório")
    @Size(max = 50, message = "unidadeId deve ter no máximo 50 caracteres")
    private String unidadeId;

    @NotNull(message = "dataHora é obrigatório")
    private LocalDateTime dataHora;

    @NotBlank(message = "profissionalId é obrigatório")
    @Size(max = 50, message = "profissionalId deve ter no máximo 50 caracteres")
    private String profissionalId;

    @NotNull(message = "tipo é obrigatório")
    private TipoAgendamento tipo;

    public ConfirmacaoRequisicao() {}

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

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
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
}
