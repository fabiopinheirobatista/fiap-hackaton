package br.com.fiap.jackaton.dto;

import br.com.fiap.jackaton.enums.TipoAgendamento;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class AgendamentoRequisicao {
    @NotBlank(message = "pacienteId é obrigatório")
    private String pacienteId;

    @NotNull(message = "tipo é obrigatório")
    private TipoAgendamento tipo;

    @Size(max = 500, message = "sintomas deve ter no máximo 500 caracteres")
    private String sintomas;

    @NotBlank(message = "localizacao é obrigatório")
    private String localizacao;

    @Min(value = 1, message = "urgencia deve ser entre 1 e 5")
    @Max(value = 5, message = "urgencia deve ser entre 1 e 5")
    private int urgencia = 1;

    public AgendamentoRequisicao() {}

    public String getPacienteId() {
        return pacienteId;
    }

    public void setPacienteId(String pacienteId) {
        this.pacienteId = pacienteId;
    }

    public TipoAgendamento getTipo() {
        return tipo;
    }

    public void setTipo(TipoAgendamento tipo) {
        this.tipo = tipo;
    }

    public String getSintomas() {
        return sintomas;
    }

    public void setSintomas(String sintomas) {
        this.sintomas = sintomas;
    }

    public String getLocalizacao() {
        return localizacao;
    }

    public void setLocalizacao(String localizacao) {
        this.localizacao = localizacao;
    }

    public int getUrgencia() {
        return urgencia;
    }

    public void setUrgencia(int urgencia) {
        this.urgencia = urgencia;
    }
}
