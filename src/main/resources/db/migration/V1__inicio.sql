CREATE TABLE IF NOT EXISTS pacientes (
  id VARCHAR(50) NOT NULL PRIMARY KEY,
  nome VARCHAR(255) NOT NULL,
  ativo BOOLEAN NOT NULL
);

CREATE TABLE IF NOT EXISTS unidades (
  id VARCHAR(50) NOT NULL PRIMARY KEY,
  nome VARCHAR(255) NOT NULL,
  localizacao VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS unidade_tipos_suportados (
  unidade_id VARCHAR(50) NOT NULL,
  tipo VARCHAR(100) NOT NULL,
  CONSTRAINT fk_unidade_tipos_suportados FOREIGN KEY (unidade_id) REFERENCES unidades(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS horarios_disponiveis (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  data_hora TIMESTAMP NOT NULL,
  id_profissional VARCHAR(50),
  unidade_id VARCHAR(50) NOT NULL,
  CONSTRAINT fk_horario_unidade FOREIGN KEY (unidade_id) REFERENCES unidades(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS agendamentos (
  id VARCHAR(50) NOT NULL PRIMARY KEY,
  paciente_id VARCHAR(50) NOT NULL,
  unidade_id VARCHAR(50) NOT NULL,
  id_profissional VARCHAR(50),
  tipo VARCHAR(100) NOT NULL,
  data_hora TIMESTAMP NOT NULL,
  status VARCHAR(50) NOT NULL,
  CONSTRAINT fk_agendamento_paciente FOREIGN KEY (paciente_id) REFERENCES pacientes(id),
  CONSTRAINT fk_agendamento_unidade FOREIGN KEY (unidade_id) REFERENCES unidades(id)
);

CREATE TABLE IF NOT EXISTS triagens (
  id VARCHAR(36) NOT NULL PRIMARY KEY,
  paciente_id VARCHAR(50) NOT NULL,
  sintomas VARCHAR(500),
  urgencia INT NOT NULL,
  motivo VARCHAR(100),
  CONSTRAINT fk_triagem_paciente FOREIGN KEY (paciente_id) REFERENCES pacientes(id)
);
