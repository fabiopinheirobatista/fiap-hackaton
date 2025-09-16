CREATE TABLE bloqueios (
  id VARCHAR(50) NOT NULL PRIMARY KEY,
  unidade_id VARCHAR(50) NOT NULL,
  data_hora TIMESTAMP NOT NULL,
  id_profissional VARCHAR(50),
  motivo VARCHAR(255),
  CONSTRAINT fk_bloqueio_unidade FOREIGN KEY (unidade_id) REFERENCES unidades(id) ON DELETE CASCADE
);

CREATE TABLE lista_espera (
  id VARCHAR(50) NOT NULL PRIMARY KEY,
  paciente_id VARCHAR(50) NOT NULL,
  tipo VARCHAR(100) NOT NULL,
  unidade_id VARCHAR(50),
  criado_em TIMESTAMP NOT NULL,
  CONSTRAINT fk_lista_paciente FOREIGN KEY (paciente_id) REFERENCES pacientes(id) ON DELETE CASCADE,
  CONSTRAINT fk_lista_unidade FOREIGN KEY (unidade_id) REFERENCES unidades(id) ON DELETE CASCADE
);

