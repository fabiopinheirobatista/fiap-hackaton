-- Inserção de dados de teste para facilitar o uso da aplicação

-- Pacientes
INSERT INTO pacientes (id, nome, ativo) VALUES
('p1', 'João Silva', true),
('p2', 'Maria Oliveira', true),
('p3', 'Carlos Santos', true);

-- Unidades
INSERT INTO unidades (id, nome, localizacao) VALUES
('u1', 'Clínica Central', 'Centro'),
('u2', 'Hospital Zona Sul', 'Zona Sul'),
('u3', 'Posto de Saúde Norte', 'Zona Norte');

-- Tipos suportados por unidade
INSERT INTO unidade_tipos_suportados (unidade_id, tipo) VALUES
('u1', 'CLINICO_GERAL'),
('u1', 'CARDIOLOGIA'),
('u1', 'ORTOPEDIA'),
('u2', 'CLINICO_GERAL'),
('u2', 'EXAME_SANGUE'),
('u2', 'PEDIATRIA'),
('u3', 'CLINICO_GERAL'),
('u3', 'GINECOLOGIA');

-- Profissionais para horários disponíveis
-- Note: Usando os IDs de profissionais que estão na requisição do Postman

-- Horários disponíveis
INSERT INTO horarios_disponiveis (data_hora, id_profissional, unidade_id) VALUES
-- Unidade 1 - Clínica Central
('2025-09-17T09:00:00', 'prof1', 'u1'),
('2025-09-17T10:00:00', 'prof1', 'u1'),
('2025-09-17T11:00:00', 'prof1', 'u1'),
('2025-09-17T14:00:00', 'prof1', 'u1'),
('2025-09-17T15:00:00', 'prof1', 'u1'),
('2025-09-17T16:00:00', 'prof1', 'u1'),
('2025-09-18T09:00:00', 'prof1', 'u1'),
('2025-09-18T10:00:00', 'prof1', 'u1'),
('2025-09-18T11:00:00', 'prof1', 'u1'),
('2025-09-25T09:00:00', 'prof1', 'u1'),
('2025-09-25T10:00:00', 'prof1', 'u1'),

-- Unidade 2 - Hospital Zona Sul
('2025-09-17T08:00:00', 'prof2', 'u2'),
('2025-09-17T09:00:00', 'prof2', 'u2'),
('2025-09-17T10:00:00', 'prof2', 'u2'),
('2025-09-17T13:00:00', 'prof2', 'u2'),
('2025-09-17T14:00:00', 'prof2', 'u2'),
('2025-09-17T15:00:00', 'prof2', 'u2'),
('2025-09-18T08:00:00', 'prof2', 'u2'),
('2025-09-18T09:00:00', 'prof2', 'u2'),
('2025-09-18T10:00:00', 'prof2', 'u2'),

-- Unidade 3 - Posto de Saúde Norte
('2025-09-17T08:30:00', 'prof3', 'u3'),
('2025-09-17T09:30:00', 'prof3', 'u3'),
('2025-09-17T10:30:00', 'prof3', 'u3'),
('2025-09-17T13:30:00', 'prof3', 'u3'),
('2025-09-17T14:30:00', 'prof3', 'u3'),
('2025-09-17T15:30:00', 'prof3', 'u3'),
('2025-09-18T08:30:00', 'prof3', 'u3'),
('2025-09-18T09:30:00', 'prof3', 'u3'),
('2025-09-18T10:30:00', 'prof3', 'u3');

-- Tabela de triagem (verificando se existe)
INSERT INTO triagens (id, paciente_id, sintomas, urgencia, motivo)
SELECT 't1', 'p1', 'sangramento intenso', 5, 'urgente'
WHERE EXISTS (SELECT 1 FROM information_schema.tables WHERE table_name = 'triagens');

INSERT INTO triagens (id, paciente_id, sintomas, urgencia, motivo)
SELECT 't2', 'p2', 'pré-operatório cirurgia cardíaca', 4, 'pré-operatório'
WHERE EXISTS (SELECT 1 FROM information_schema.tables WHERE table_name = 'triagens');
