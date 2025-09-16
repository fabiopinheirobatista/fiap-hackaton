package br.com.fiap.jackaton.dto;

import br.com.fiap.jackaton.enums.TipoAgendamento;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class RecusaRequisicao {
    @NotBlank(message = "pacienteId é obrigatório")
    private String pacienteId;

    @NotBlank(message = "unidadeId da sugestão recusada é obrigatório")
    private String unidadeId;

    @NotNull(message = "dataHora da sugestão recusada é obrigatória")
    private LocalDateTime dataHora;

    private String profissionalId;

    @NotNull(message = "tipo é obrigatório")
    private TipoAgendamento tipo;

    @NotBlank(message = "localizacao é obrigatória")
    private String localizacao;

    private int urgencia = 1;

    private String motivoRecusa;

    public RecusaRequisicao() {}

    public String getPacienteId() { return pacienteId; }
    public void setPacienteId(String pacienteId) { this.pacienteId = pacienteId; }

    public String getUnidadeId() { return unidadeId; }
    public void setUnidadeId(String unidadeId) { this.unidadeId = unidadeId; }

    public LocalDateTime getDataHora() { return dataHora; }
    public void setDataHora(LocalDateTime dataHora) { this.dataHora = dataHora; }

    public String getProfissionalId() { return profissionalId; }
    public void setProfissionalId(String profissionalId) { this.profissionalId = profissionalId; }

    public TipoAgendamento getTipo() { return tipo; }
    public void setTipo(TipoAgendamento tipo) { this.tipo = tipo; }

    public String getLocalizacao() { return localizacao; }
    public void setLocalizacao(String localizacao) { this.localizacao = localizacao; }

    public int getUrgencia() { return urgencia; }
    public void setUrgencia(int urgencia) { this.urgencia = urgencia; }

    public String getMotivoRecusa() { return motivoRecusa; }
    public void setMotivoRecusa(String motivoRecusa) { this.motivoRecusa = motivoRecusa; }
}

