package br.com.fiap.hackaton.dto;

import br.com.fiap.hackaton.enums.StatusAgendamento;
import br.com.fiap.hackaton.enums.TipoAgendamento;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class ConsultaPacienteResposta {

    @NotNull
    private String id;

    @NotNull
    private LocalDateTime dataHora;

    @NotNull
    private String unidadeNome;

    private String unidadeLocalizacao;

    private String profissionalId;

    @NotNull
    private TipoAgendamento tipoAtendimento;

    @NotNull
    private StatusAgendamento status;

    public ConsultaPacienteResposta() {}

    public ConsultaPacienteResposta(String id, LocalDateTime dataHora, String unidadeNome,
                                   String unidadeLocalizacao, String profissionalId,
                                   TipoAgendamento tipoAtendimento, StatusAgendamento status) {
        this.id = id;
        this.dataHora = dataHora;
        this.unidadeNome = unidadeNome;
        this.unidadeLocalizacao = unidadeLocalizacao;
        this.profissionalId = profissionalId;
        this.tipoAtendimento = tipoAtendimento;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public String getUnidadeNome() {
        return unidadeNome;
    }

    public void setUnidadeNome(String unidadeNome) {
        this.unidadeNome = unidadeNome;
    }

    public String getUnidadeLocalizacao() {
        return unidadeLocalizacao;
    }

    public void setUnidadeLocalizacao(String unidadeLocalizacao) {
        this.unidadeLocalizacao = unidadeLocalizacao;
    }

    public String getProfissionalId() {
        return profissionalId;
    }

    public void setProfissionalId(String profissionalId) {
        this.profissionalId = profissionalId;
    }

    public TipoAgendamento getTipoAtendimento() {
        return tipoAtendimento;
    }

    public void setTipoAtendimento(TipoAgendamento tipoAtendimento) {
        this.tipoAtendimento = tipoAtendimento;
    }

    public StatusAgendamento getStatus() {
        return status;
    }

    public void setStatus(StatusAgendamento status) {
        this.status = status;
    }
}
