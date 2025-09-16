package br.com.fiap.hackaton.dto;

import java.time.LocalDateTime;

public class SugestaoResposta {
    private String unidadeId;
    private String nomeUnidade;
    private LocalDateTime dataHora;
    private String profissionalId;

    public SugestaoResposta() {}

    public SugestaoResposta(String unidadeId, String nomeUnidade, LocalDateTime dataHora, String profissionalId) {
        this.unidadeId = unidadeId;
        this.nomeUnidade = nomeUnidade;
        this.dataHora = dataHora;
        this.profissionalId = profissionalId;
    }

    public String getUnidadeId() {
        return unidadeId;
    }

    public void setUnidadeId(String unidadeId) {
        this.unidadeId = unidadeId;
    }

    public String getNomeUnidade() {
        return nomeUnidade;
    }

    public void setNomeUnidade(String nomeUnidade) {
        this.nomeUnidade = nomeUnidade;
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
}
