package br.com.fiap.jackaton.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "triagens")
public class TriagemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "paciente_id", length = 50, nullable = false)
    private String patientId;

    @Column(length = 500)
    private String sintomas;

    @Column(nullable = false)
    private int urgencia;

    @Column(length = 100)
    private String motivo;

    public TriagemEntity() {}

    public TriagemEntity(String patientId, String sintomas, int urgencia, String motivo) {
        this.patientId = patientId;
        this.sintomas = sintomas;
        this.urgencia = urgencia;
        this.motivo = motivo;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getPatientId() { return patientId; }
    public void setPatientId(String patientId) { this.patientId = patientId; }
    public String getSintomas() { return sintomas; }
    public void setSintomas(String sintomas) { this.sintomas = sintomas; }
    public int getUrgencia() { return urgencia; }
    public void setUrgencia(int urgencia) { this.urgencia = urgencia; }
    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }
}
