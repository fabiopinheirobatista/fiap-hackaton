package br.com.fiap.jackaton.dto;

import br.com.fiap.jackaton.enums.TipoAgendamento;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public class AgendaRequest {

    @NotBlank(message = "unidadeId é obrigatório")
    @Size(max = 50, message = "unidadeId deve ter no máximo 50 caracteres")
    private String unidadeId;

    @NotNull(message = "data é obrigatória")
    private LocalDate data;

    @NotNull(message = "especialidade é obrigatória")
    private TipoAgendamento especialidade;

    public AgendaRequest() {}

    public String getUnidadeId() { return unidadeId; }
    public void setUnidadeId(String unidadeId) { this.unidadeId = unidadeId; }
    public LocalDate getData() { return data; }
    public void setData(LocalDate data) { this.data = data; }
    public TipoAgendamento getEspecialidade() { return especialidade; }
    public void setEspecialidade(TipoAgendamento especialidade) { this.especialidade = especialidade; }
}
