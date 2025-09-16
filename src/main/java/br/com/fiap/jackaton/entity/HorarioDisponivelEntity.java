package br.com.fiap.jackaton.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "horarios_disponiveis")
public class HorarioDisponivelEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "data_hora", nullable = false)
    private LocalDateTime dataHora;

    @Column(name = "id_profissional")
    private String idProfissional;

    @ManyToOne
    @JoinColumn(name = "unidade_id", nullable = false)
    private UnidadeEntity unidade;

    public HorarioDisponivelEntity() {
    }

    public HorarioDisponivelEntity(LocalDateTime dataHora, String idProfissional) {
        this.dataHora = dataHora;
        this.idProfissional = idProfissional;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public String getIdProfissional() {
        return idProfissional;
    }

    public void setIdProfissional(String idProfissional) {
        this.idProfissional = idProfissional;
    }

    public UnidadeEntity getUnidade() {
        return unidade;
    }

    public void setUnidade(UnidadeEntity unidade) {
        this.unidade = unidade;
    }
}
