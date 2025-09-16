package br.com.fiap.hackaton.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "lista_espera")
public class ListaEsperaEntity {

    @Id
    @Column(length = 50)
    private String id;

    @Column(name = "paciente_id", length = 50, nullable = false)
    private String pacienteId;

    @Column(length = 100, nullable = false)
    private String tipo;

    @ManyToOne
    @JoinColumn(name = "unidade_id")
    private UnidadeEntity unidade;

    @Column(name = "criado_em", nullable = false)
    private LocalDateTime criadoEm;

    public ListaEsperaEntity() {}

    public ListaEsperaEntity(String id, String pacienteId, String tipo, UnidadeEntity unidade, LocalDateTime criadoEm) {
        this.id = id;
        this.pacienteId = pacienteId;
        this.tipo = tipo;
        this.unidade = unidade;
        this.criadoEm = criadoEm;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getPacienteId() { return pacienteId; }
    public void setPacienteId(String pacienteId) { this.pacienteId = pacienteId; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public UnidadeEntity getUnidade() { return unidade; }
    public void setUnidade(UnidadeEntity unidade) { this.unidade = unidade; }
    public LocalDateTime getCriadoEm() { return criadoEm; }
    public void setCriadoEm(LocalDateTime criadoEm) { this.criadoEm = criadoEm; }
}

