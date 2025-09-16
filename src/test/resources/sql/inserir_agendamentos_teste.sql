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
INSERT INTO agendamentos (id, paciente_id, unidade_id, id_profissional, tipo, data_hora, status) VALUES
('a1', 'p1', 'u1', 'prof1', 'CLINICO_GERAL', DATEADD('DAY', 1, CURRENT_TIMESTAMP()), 'AGENDADA'),
('a2', 'p1', 'u2', 'prof2', 'EXAME_SANGUE', DATEADD('DAY', 3, CURRENT_TIMESTAMP()), 'CONFIRMADA'),
('a3', 'p2', 'u1', 'prof1', 'PEDIATRIA', DATEADD('DAY', 2, CURRENT_TIMESTAMP()), 'AGENDADA'),
('a4', 'p3', 'u2', 'prof3', 'CARDIOLOGIA', DATEADD('DAY', 5, CURRENT_TIMESTAMP()), 'REAGENDADA');

-- Insere agendamentos passados (histórico)
INSERT INTO agendamentos (id, paciente_id, unidade_id, id_profissional, tipo, data_hora, status) VALUES
('a5', 'p1', 'u1', 'prof1', 'CLINICO_GERAL', DATEADD('DAY', -5, CURRENT_TIMESTAMP()), 'CONFIRMADA'),
('a6', 'p1', 'u2', 'prof2', 'EXAME_SANGUE', DATEADD('DAY', -10, CURRENT_TIMESTAMP()), 'CONFIRMADA'),
('a7', 'p2', 'u1', 'prof1', 'PEDIATRIA', DATEADD('DAY', -7, CURRENT_TIMESTAMP()), 'CANCELADA'),
('a8', 'p2', 'u2', 'prof3', 'CARDIOLOGIA', DATEADD('DAY', -15, CURRENT_TIMESTAMP()), 'CONFIRMADA');
