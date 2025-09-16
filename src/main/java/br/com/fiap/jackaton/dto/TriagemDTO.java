package br.com.fiap.jackaton.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class TriagemDTO {
    @NotBlank(message = "pacienteId é obrigatório")
    private String pacienteId;

    @Size(max = 500, message = "sintomas deve ter no máximo 500 caracteres")
    private String sintomas;

    @NotNull(message = "urgencia é obrigatória")
    private Integer urgencia;

    @Size(max = 100, message = "motivo deve ter no máximo 100 caracteres")
    private String motivo;

    public TriagemDTO() {}

    public String getPacienteId() { return pacienteId; }
    public void setPacienteId(String pacienteId) { this.pacienteId = pacienteId; }
    public String getSintomas() { return sintomas; }
    public void setSintomas(String sintomas) { this.sintomas = sintomas; }
    public Integer getUrgencia() { return urgencia; }
    public void setUrgencia(Integer urgencia) { this.urgencia = urgencia; }
    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }
}
