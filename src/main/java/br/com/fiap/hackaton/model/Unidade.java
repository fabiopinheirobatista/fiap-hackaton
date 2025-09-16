package br.com.fiap.hackaton.model;

import br.com.fiap.hackaton.enums.TipoAgendamento;

import java.util.List;
import java.util.Objects;

public class Unidade {
    private final String id;
    private final String nome;
    private final String localizacao;
    private final List<HorarioDisponivel> horariosDisponiveis;

    public Unidade(String id, String nome, String localizacao, List<TipoAgendamento> supportedTypes, List<HorarioDisponivel> horariosDisponiveis) {
        this.id = id;
        this.nome = nome;
        this.localizacao = localizacao;
        this.horariosDisponiveis = horariosDisponiveis;
    }

    public String getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getLocalizacao() {
        return localizacao;
    }

    public List<HorarioDisponivel> getHorariosDisponiveis() {
        return horariosDisponiveis;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Unidade unidade = (Unidade) o;
        return Objects.equals(id, unidade.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
