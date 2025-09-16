package br.com.fiap.hackaton.dto;

import br.com.fiap.hackaton.enums.TipoAgendamento;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.time.LocalDate;
import java.util.List;

public class AgendaResponse {

    @NotBlank(message = "unidadeId é obrigatório")
    private String unidadeId;

    @NotBlank(message = "nomeUnidade é obrigatório")
    private String nomeUnidade;

    @NotNull(message = "data é obrigatória")
    private LocalDate data;

    @NotNull(message = "especialidade é obrigatória")
    private TipoAgendamento especialidade;

    @Valid
    private List<ProfissionalAgendaDTO> profissionais;

    @PositiveOrZero(message = "pacientesNaEspera deve ser positivo ou zero")
    private int pacientesNaEspera;

    public AgendaResponse() {}

    public AgendaResponse(String unidadeId, String nomeUnidade, LocalDate data, TipoAgendamento especialidade,
                         List<ProfissionalAgendaDTO> profissionais, int pacientesNaEspera) {
        this.unidadeId = unidadeId;
        this.nomeUnidade = nomeUnidade;
        this.data = data;
        this.especialidade = especialidade;
        this.profissionais = profissionais;
        this.pacientesNaEspera = pacientesNaEspera;
    }

    public String getUnidadeId() { return unidadeId; }
    public void setUnidadeId(String unidadeId) { this.unidadeId = unidadeId; }
    public String getNomeUnidade() { return nomeUnidade; }
    public void setNomeUnidade(String nomeUnidade) { this.nomeUnidade = nomeUnidade; }
    public LocalDate getData() { return data; }
    public void setData(LocalDate data) { this.data = data; }
    public TipoAgendamento getEspecialidade() { return especialidade; }
    public void setEspecialidade(TipoAgendamento especialidade) { this.especialidade = especialidade; }
    public List<ProfissionalAgendaDTO> getProfissionais() { return profissionais; }
    public void setProfissionais(List<ProfissionalAgendaDTO> profissionais) { this.profissionais = profissionais; }
    public int getPacientesNaEspera() { return pacientesNaEspera; }
    public void setPacientesNaEspera(int pacientesNaEspera) { this.pacientesNaEspera = pacientesNaEspera; }
}
