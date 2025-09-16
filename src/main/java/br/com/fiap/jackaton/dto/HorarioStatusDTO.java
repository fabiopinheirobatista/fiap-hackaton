package br.com.fiap.jackaton.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public class HorarioStatusDTO {
    @NotNull(message = "dataHora é obrigatória")
    private LocalDateTime dataHora;

    @NotBlank(message = "status é obrigatório")
    @Pattern(regexp = "VAGO|OCUPADO|BLOQUEADO", message = "status deve ser VAGO, OCUPADO ou BLOQUEADO")
    private String status;

    @Size(max = 255, message = "observacao deve ter no máximo 255 caracteres")
    private String observacao;

    public HorarioStatusDTO() {}

    public HorarioStatusDTO(LocalDateTime dataHora, String status) {
        this.dataHora = dataHora;
        this.status = status;
    }

    public HorarioStatusDTO(LocalDateTime dataHora, String status, String observacao) {
        this.dataHora = dataHora;
        this.status = status;
        this.observacao = observacao;
    }

    public LocalDateTime getDataHora() { return dataHora; }
    public void setDataHora(LocalDateTime dataHora) { this.dataHora = dataHora; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getObservacao() { return observacao; }
    public void setObservacao(String observacao) { this.observacao = observacao; }
}
