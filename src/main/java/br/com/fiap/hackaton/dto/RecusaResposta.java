package br.com.fiap.hackaton.dto;

public class RecusaResposta {
    private String mensagem;
    private SugestaoResposta sugestao;

    public RecusaResposta() {}

    public RecusaResposta(String mensagem, SugestaoResposta sugestao) {
        this.mensagem = mensagem;
        this.sugestao = sugestao;
    }

    public String getMensagem() { return mensagem; }
    public void setMensagem(String mensagem) { this.mensagem = mensagem; }

    public SugestaoResposta getSugestao() { return sugestao; }
    public void setSugestao(SugestaoResposta sugestao) { this.sugestao = sugestao; }
}

