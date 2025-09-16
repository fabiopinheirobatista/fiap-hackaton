package br.com.fiap.jackaton.entity;

import br.com.fiap.jackaton.enums.StatusAgendamento;
import br.com.fiap.jackaton.enums.TipoAgendamento;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "agendamentos")
public class AgendamentoEntity {

    @Id
    @Column(length = 50)
    private String id;

    @Column(length = 50, nullable = false)
    private String pacienteId;

    @Column(length = 50, nullable = false)
    private String unidadeId;

    @Column(name = "id_profissional", length = 50)
    private String profissionalId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoAgendamento tipo;

    @Column(nullable = false)
    private LocalDateTime dataHora;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusAgendamento status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "triagem_id")
    private TriagemEntity triagem;

    public AgendamentoEntity() {}

    public AgendamentoEntity(String id, String pacienteId, String unidadeId, String profissionalId, TipoAgendamento tipo, LocalDateTime dataHora, StatusAgendamento status) {
        this.id = id;
        this.pacienteId = pacienteId;
        this.unidadeId = unidadeId;
        this.profissionalId = profissionalId;
        this.tipo = tipo;
        this.dataHora = dataHora;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPacienteId() {
        return pacienteId;
    }

    public void setPacienteId(String pacienteId) {
        this.pacienteId = pacienteId;
    }

    public String getUnidadeId() {
        return unidadeId;
    }

    public void setUnidadeId(String unidadeId) {
        this.unidadeId = unidadeId;
    }

    public String getProfissionalId() {
        return profissionalId;
    }

    public void setProfissionalId(String profissionalId) {
        this.profissionalId = profissionalId;
    }

    public TipoAgendamento getTipo() {
        return tipo;
    }

    public void setTipo(TipoAgendamento tipo) {
        this.tipo = tipo;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public StatusAgendamento getStatus() {
        return status;
    }

    public void setStatus(StatusAgendamento status) {
        this.status = status;
    }

    public TriagemEntity getTriagem() {
        return triagem;
    }

    public void setTriagem(TriagemEntity triagem) {
        this.triagem = triagem;
    }
}
