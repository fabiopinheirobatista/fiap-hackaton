package br.com.fiap.hackaton.repository;

import br.com.fiap.hackaton.entity.AgendamentoEntity;
import br.com.fiap.hackaton.enums.StatusAgendamento;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ImportAutoConfiguration(exclude = FlywayAutoConfiguration.class)
@TestPropertySource(properties = {"spring.flyway.enabled=false","spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration"})
public class AgendamentoRepositoryTest {

    @Autowired
    private AgendamentoRepository agendamentoRepository;

    @Test
    @DisplayName("Deve retornar agendamentos ativos do paciente")
    @Sql("/sql/inserir_agendamentos_teste.sql")
    public void testFindAgendamentosAtivosPorPaciente() {
        String pacienteId = "p1";
        LocalDateTime agora = LocalDateTime.now();
        List<StatusAgendamento> statusAtivos = Arrays.asList(
            StatusAgendamento.AGENDADA,
            StatusAgendamento.CONFIRMADA,
            StatusAgendamento.REAGENDADA
        );

        List<AgendamentoEntity> agendamentos = agendamentoRepository
            .findAgendamentosAtivosPorPaciente(pacienteId, agora, statusAtivos);

        assertTrue(!agendamentos.isEmpty(), "Deve encontrar pelo menos um agendamento ativo");

        for (AgendamentoEntity agendamento : agendamentos) {
            assertEquals(pacienteId, agendamento.getPacienteId(), "Deve pertencer ao paciente correto");
            assertTrue(agendamento.getDataHora().isAfter(agora), "Deve ser uma data futura");
            assertTrue(statusAtivos.contains(agendamento.getStatus()), "Deve ter um status ativo");
        }
    }

    @Test
    @DisplayName("Deve retornar histórico de atendimentos do paciente")
    @Sql("/sql/inserir_agendamentos_teste.sql")
    public void testFindHistoricoAtendimentosPorPaciente() {
        String pacienteId = "p1";
        LocalDateTime agora = LocalDateTime.now();

        List<AgendamentoEntity> agendamentos = agendamentoRepository
            .findHistoricoAtendimentosPorPaciente(pacienteId, agora);

        for (AgendamentoEntity agendamento : agendamentos) {
            assertEquals(pacienteId, agendamento.getPacienteId(), "Deve pertencer ao paciente correto");
            assertTrue(agendamento.getDataHora().isBefore(agora), "Deve ser uma data passada");
        }
    }

    @Test
    @DisplayName("Deve retornar todos os agendamentos de um paciente")
    @Sql("/sql/inserir_agendamentos_teste.sql")
    public void testFindAllByPacienteId() {
        String pacienteId = "p1";

        List<AgendamentoEntity> agendamentos = agendamentoRepository.findAllByPacienteId(pacienteId);

        assertTrue(!agendamentos.isEmpty(), "Deve encontrar pelo menos um agendamento");

        for (AgendamentoEntity agendamento : agendamentos) {
            assertEquals(pacienteId, agendamento.getPacienteId(), "Deve pertencer ao paciente correto");
        }
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando paciente não tem agendamentos")
    public void testPacienteSemAgendamentos() {
        String pacienteInexistente = "paciente_inexistente";
        LocalDateTime agora = LocalDateTime.now();
        List<StatusAgendamento> statusAtivos = Arrays.asList(
            StatusAgendamento.AGENDADA,
            StatusAgendamento.CONFIRMADA,
            StatusAgendamento.REAGENDADA
        );

        List<AgendamentoEntity> agendamentosAtivos = agendamentoRepository
            .findAgendamentosAtivosPorPaciente(pacienteInexistente, agora, statusAtivos);

        List<AgendamentoEntity> historico = agendamentoRepository
            .findHistoricoAtendimentosPorPaciente(pacienteInexistente, agora);

        List<AgendamentoEntity> todos = agendamentoRepository
            .findAllByPacienteId(pacienteInexistente);

        assertTrue(agendamentosAtivos.isEmpty(), "Não deve encontrar agendamentos ativos");
        assertTrue(historico.isEmpty(), "Não deve encontrar histórico de agendamentos");
        assertTrue(todos.isEmpty(), "Não deve encontrar nenhum agendamento");
    }
}
