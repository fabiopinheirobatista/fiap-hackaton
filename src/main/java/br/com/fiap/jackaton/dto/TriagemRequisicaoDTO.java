package br.com.fiap.jackaton.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public class TriagemRequisicaoDTO {
    @NotNull(message = "requisição é obrigatória")
    @Valid
    private AgendamentoRequisicao requisicao;

    @NotNull(message = "triagem é obrigatória")
    @Valid
    private TriagemDTO triagem;

    public TriagemRequisicaoDTO() {}

    public AgendamentoRequisicao getRequisicao() {
        return requisicao;
    }

    public void setRequisicao(AgendamentoRequisicao requisicao) {
        this.requisicao = requisicao;
    }

    public TriagemDTO getTriagem() {
        return triagem;
    }

    public void setTriagem(TriagemDTO triagem) {
        this.triagem = triagem;
    }
}
