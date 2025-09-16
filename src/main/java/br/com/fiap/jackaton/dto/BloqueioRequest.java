package br.com.fiap.jackaton.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public class BloqueioRequest {

    @NotBlank(message = "unidadeId é obrigatório")
    @Size(max = 50, message = "unidadeId deve ter no máximo 50 caracteres")
    private String unidadeId;

    @NotNull(message = "dataHora é obrigatória")
    private LocalDateTime dataHora;

    @Size(max = 50, message = "profissionalId deve ter no máximo 50 caracteres")
    private String profissionalId;

    @NotBlank(message = "motivo é obrigatório")
    @Size(max = 255, message = "motivo deve ter no máximo 255 caracteres")
    private String motivo;

    public BloqueioRequest() {}

    public String getUnidadeId() { return unidadeId; }
    public void setUnidadeId(String unidadeId) { this.unidadeId = unidadeId; }
    public LocalDateTime getDataHora() { return dataHora; }
    public void setDataHora(LocalDateTime dataHora) { this.dataHora = dataHora; }
    public String getProfissionalId() { return profissionalId; }
    public void setProfissionalId(String profissionalId) { this.profissionalId = profissionalId; }
    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }
}
