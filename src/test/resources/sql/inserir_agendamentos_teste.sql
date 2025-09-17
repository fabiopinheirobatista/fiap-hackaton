-- Limpa dados existentes para os testes
DELETE FROM agendamentos WHERE paciente_id IN ('p1', 'p2', 'p3');
DELETE FROM unidades WHERE id IN ('u1', 'u2');
DELETE FROM pacientes WHERE id IN ('p1', 'p2', 'p3');

-- Insere unidades para teste
INSERT INTO unidades (id, nome, localizacao) VALUES
('u1', 'Unidade Central', 'Centro'),
('u2', 'Unidade Sul', 'Zona Sul');

-- Insere pacientes para teste
INSERT INTO pacientes (id, nome, ativo) VALUES
('p1', 'Paciente Teste 1', true),
('p2', 'Paciente Teste 2', true),
('p3', 'Paciente Teste 3', true);

-- Insere agendamentos futuros (ativos)
-- Usar função compatível com MySQL para adicionar dias: DATE_ADD(CURRENT_TIMESTAMP(), INTERVAL n DAY)
INSERT INTO agendamentos (id, paciente_id, unidade_id, id_profissional, tipo, data_hora, status) VALUES
('a1', 'p1', 'u1', 'prof1', 'CLINICO_GERAL', DATE_ADD(CURRENT_TIMESTAMP(), INTERVAL 1 DAY), 'AGENDADA'),
('a2', 'p1', 'u2', 'prof2', 'EXAME_SANGUE', DATE_ADD(CURRENT_TIMESTAMP(), INTERVAL 3 DAY), 'CONFIRMADA'),
('a3', 'p2', 'u1', 'prof1', 'PEDIATRIA', DATE_ADD(CURRENT_TIMESTAMP(), INTERVAL 2 DAY), 'AGENDADA'),
('a4', 'p3', 'u2', 'prof3', 'CARDIOLOGIA', DATE_ADD(CURRENT_TIMESTAMP(), INTERVAL 5 DAY), 'REAGENDADA');

-- Insere agendamentos passados (histórico)
INSERT INTO agendamentos (id, paciente_id, unidade_id, id_profissional, tipo, data_hora, status) VALUES
('a5', 'p1', 'u1', 'prof1', 'CLINICO_GERAL', DATE_SUB(CURRENT_TIMESTAMP(), INTERVAL 5 DAY), 'CONFIRMADA'),
('a6', 'p1', 'u2', 'prof2', 'EXAME_SANGUE', DATE_SUB(CURRENT_TIMESTAMP(), INTERVAL 10 DAY), 'CONFIRMADA'),
('a7', 'p2', 'u1', 'prof1', 'PEDIATRIA', DATE_SUB(CURRENT_TIMESTAMP(), INTERVAL 7 DAY), 'CANCELADA'),
('a8', 'p2', 'u2', 'prof3', 'CARDIOLOGIA', DATE_SUB(CURRENT_TIMESTAMP(), INTERVAL 15 DAY), 'CONFIRMADA');
