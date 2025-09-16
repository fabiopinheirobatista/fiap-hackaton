package br.com.fiap.hackaton.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

public class ProfissionalAgendaDTO {
    @NotBlank(message = "profissionalId é obrigatório")
    @Size(max = 50, message = "profissionalId deve ter no máximo 50 caracteres")
    private String profissionalId;

    @NotBlank(message = "nomeProfissional é obrigatório")
    @Size(max = 200, message = "nomeProfissional deve ter no máximo 200 caracteres")
    private String nomeProfissional;

    @Valid
    private List<HorarioStatusDTO> horarios;

    public ProfissionalAgendaDTO() {}

    public ProfissionalAgendaDTO(String profissionalId, String nomeProfissional, List<HorarioStatusDTO> horarios) {
        this.profissionalId = profissionalId;
        this.nomeProfissional = nomeProfissional;
        this.horarios = horarios;
    }

    public String getProfissionalId() { return profissionalId; }
    public void setProfissionalId(String profissionalId) { this.profissionalId = profissionalId; }
    public String getNomeProfissional() { return nomeProfissional; }
    public void setNomeProfissional(String nomeProfissional) { this.nomeProfissional = nomeProfissional; }
    public List<HorarioStatusDTO> getHorarios() { return horarios; }
    public void setHorarios(List<HorarioStatusDTO> horarios) { this.horarios = horarios; }
}
