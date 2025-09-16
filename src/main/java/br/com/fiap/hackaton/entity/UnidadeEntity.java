package br.com.fiap.hackaton.entity;

import br.com.fiap.hackaton.enums.TipoAgendamento;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "unidades")
public class UnidadeEntity {
    @Id
    private String id;

    @Column(nullable = false)
    private String nome;

    @Column
    private String localizacao;

    @ElementCollection
    @CollectionTable(name = "unidade_tipos_suportados", joinColumns = @JoinColumn(name = "unidade_id"))
    @Column(name = "tipo")
    private List<TipoAgendamento> tiposSuportados = new ArrayList<>();

    @OneToMany(mappedBy = "unidade", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HorarioDisponivelEntity> horariosDisponiveis = new ArrayList<>();

    public UnidadeEntity() {
    }

    public UnidadeEntity(String id, String nome, String localizacao) {
        this.id = id;
        this.nome = nome;
        this.localizacao = localizacao;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getLocalizacao() {
        return localizacao;
    }

    public void setLocalizacao(String localizacao) {
        this.localizacao = localizacao;
    }

    public List<TipoAgendamento> getTiposSuportados() {
        return tiposSuportados;
    }

    public void setTiposSuportados(List<TipoAgendamento> tiposSuportados) {
        this.tiposSuportados = tiposSuportados;
    }

    public List<HorarioDisponivelEntity> getHorariosDisponiveis() {
        return horariosDisponiveis;
    }

    public void adicionarHorario(HorarioDisponivelEntity horario) {
        horario.setUnidade(this);
        this.horariosDisponiveis.add(horario);
    }
}
