ALTER TABLE agendamentos ADD COLUMN triagem_id VARCHAR(36);
ALTER TABLE agendamentos ADD CONSTRAINT fk_agendamento_triagem FOREIGN KEY (triagem_id) REFERENCES triagens(id);
