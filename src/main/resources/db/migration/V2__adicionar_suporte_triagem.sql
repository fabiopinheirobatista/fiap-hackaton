CREATE TABLE triagens (
  id VARCHAR(36) NOT NULL PRIMARY KEY,
  paciente_id VARCHAR(50) NOT NULL,
  sintomas VARCHAR(500),
  urgencia INT NOT NULL,
  motivo VARCHAR(100),
  CONSTRAINT fk_triagem_paciente FOREIGN KEY (paciente_id) REFERENCES pacientes(id)
);

ALTER TABLE agendamentos ADD COLUMN triagem_id VARCHAR(36);
ALTER TABLE agendamentos ADD CONSTRAINT fk_agendamento_triagem FOREIGN KEY (triagem_id) REFERENCES triagens(id);
