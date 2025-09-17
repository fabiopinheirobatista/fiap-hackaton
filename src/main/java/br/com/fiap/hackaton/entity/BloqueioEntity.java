package br.com.fiap.hackaton.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "bloqueios")
public class BloqueioEntity {

    @Id
    @Column(length = 50)
    private String id;

    @ManyToOne
    @JoinColumn(name = "unidade_id", nullable = false)
    private UnidadeEntity unidade;

    @Column(name = "data_hora", nullable = false)
    private LocalDateTime dataHora;

    @Column(name = "id_profissional")
    private String idProfissional;

    @Column(length = 255)
    private String motivo;

    public BloqueioEntity() {}

    public BloqueioEntity(String id, UnidadeEntity unidade, LocalDateTime dataHora, String idProfissional, String motivo) {
        this.id = id;
        this.unidade = unidade;
        this.dataHora = dataHora;
        this.idProfissional = idProfissional;
        this.motivo = motivo;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public UnidadeEntity getUnidade() { return unidade; }
    public void setUnidade(UnidadeEntity unidade) { this.unidade = unidade; }
    public LocalDateTime getDataHora() { return dataHora; }
    public void setDataHora(LocalDateTime dataHora) { this.dataHora = dataHora; }
    public String getIdProfissional() { return idProfissional; }
    public void setIdProfissional(String idProfissional) { this.idProfissional = idProfissional; }
    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }
}

