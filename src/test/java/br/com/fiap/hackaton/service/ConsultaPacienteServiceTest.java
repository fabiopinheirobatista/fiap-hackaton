package br.com.fiap.hackaton.service;

import br.com.fiap.hackaton.dto.ConsultaPacienteResposta;
import br.com.fiap.hackaton.entity.AgendamentoEntity;
import br.com.fiap.hackaton.entity.UnidadeEntity;
import br.com.fiap.hackaton.enums.StatusAgendamento;
import br.com.fiap.hackaton.enums.TipoAgendamento;
import br.com.fiap.hackaton.repository.AgendamentoRepository;
import br.com.fiap.hackaton.repository.UnidadeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ConsultaPacienteServiceTest {

    @Mock
    private AgendamentoRepository agendamentoRepository;

    @Mock
    private UnidadeRepository unidadeRepository;

    @InjectMocks
    private ConsultaPacienteService consultaPacienteService;

    private AgendamentoEntity agendamento1;
    private AgendamentoEntity agendamento2;
    private UnidadeEntity unidade;
    private final String PACIENTE_ID = "p1";
    private final String UNIDADE_ID = "u1";

    @BeforeEach
    public void setup() {
        unidade = new UnidadeEntity(UNIDADE_ID, "Unidade Teste", "Local Teste");

        LocalDateTime dataFutura = LocalDateTime.now().plusDays(1);
        LocalDateTime dataPassada = LocalDateTime.now().minusDays(1);

        agendamento1 = new AgendamentoEntity(
            "a1", PACIENTE_ID, UNIDADE_ID, "prof1",
            TipoAgendamento.CLINICO_GERAL, dataFutura, StatusAgendamento.AGENDADA
        );

        agendamento2 = new AgendamentoEntity(
            "a2", PACIENTE_ID, UNIDADE_ID, "prof2",
            TipoAgendamento.EXAME_SANGUE, dataPassada, StatusAgendamento.CONFIRMADA
        );
    }

    @Test
    @DisplayName("Deve retornar consultas ativas de um paciente")
    public void testBuscarConsultasAtivas() {
        List<AgendamentoEntity> agendamentosAtivos = Collections.singletonList(agendamento1);

        when(agendamentoRepository.findAgendamentosAtivosPorPaciente(
            eq(PACIENTE_ID), any(LocalDateTime.class), anyList()))
            .thenReturn(agendamentosAtivos);

        when(unidadeRepository.findAllById(anyList()))
            .thenReturn(Collections.singletonList(unidade));

        List<ConsultaPacienteResposta> resultado = consultaPacienteService.buscarConsultasAtivas(PACIENTE_ID);

        assertEquals(1, resultado.size());
        assertEquals("a1", resultado.get(0).getId());
        assertEquals("Unidade Teste", resultado.get(0).getUnidadeNome());
        assertEquals("Local Teste", resultado.get(0).getUnidadeLocalizacao());
        assertEquals(TipoAgendamento.CLINICO_GERAL, resultado.get(0).getTipoAtendimento());
    }

    @Test
    @DisplayName("Deve retornar histórico de consultas de um paciente")
    public void testBuscarHistoricoConsultas() {
        List<AgendamentoEntity> historicoAgendamentos = Collections.singletonList(agendamento2);

        when(agendamentoRepository.findHistoricoAtendimentosPorPaciente(
            eq(PACIENTE_ID), any(LocalDateTime.class)))
            .thenReturn(historicoAgendamentos);

        when(unidadeRepository.findAllById(anyList()))
            .thenReturn(Collections.singletonList(unidade));

        List<ConsultaPacienteResposta> resultado = consultaPacienteService.buscarHistoricoConsultas(PACIENTE_ID);

        assertEquals(1, resultado.size());
        assertEquals("a2", resultado.get(0).getId());
        assertEquals("Unidade Teste", resultado.get(0).getUnidadeNome());
        assertEquals("Local Teste", resultado.get(0).getUnidadeLocalizacao());
        assertEquals(TipoAgendamento.EXAME_SANGUE, resultado.get(0).getTipoAtendimento());
    }

    @Test
    @DisplayName("Deve retornar todas as consultas de um paciente")
    public void testBuscarTodasConsultas() {
        List<AgendamentoEntity> todosAgendamentos = Arrays.asList(agendamento1, agendamento2);

        when(agendamentoRepository.findAllByPacienteId(PACIENTE_ID))
            .thenReturn(todosAgendamentos);

        when(unidadeRepository.findAllById(anyList()))
            .thenReturn(Collections.singletonList(unidade));

        List<ConsultaPacienteResposta> resultado = consultaPacienteService.buscarTodasConsultas(PACIENTE_ID);

        assertEquals(2, resultado.size());
        assertEquals("a1", resultado.get(0).getId());
        assertEquals("a2", resultado.get(1).getId());
    }

    @Test
    @DisplayName("Deve lidar corretamente com paciente sem consultas")
    public void testPacienteSemConsultas() {
        when(agendamentoRepository.findAgendamentosAtivosPorPaciente(
            eq(PACIENTE_ID), any(LocalDateTime.class), anyList()))
            .thenReturn(Collections.emptyList());

        List<ConsultaPacienteResposta> resultado = consultaPacienteService.buscarConsultasAtivas(PACIENTE_ID);

        assertTrue(resultado.isEmpty());
    }

    @Test
    @DisplayName("Deve lidar corretamente com unidade não encontrada")
    public void testUnidadeNaoEncontrada() {
        List<AgendamentoEntity> agendamentosAtivos = Collections.singletonList(agendamento1);

        when(agendamentoRepository.findAgendamentosAtivosPorPaciente(
            eq(PACIENTE_ID), any(LocalDateTime.class), anyList()))
            .thenReturn(agendamentosAtivos);

        when(unidadeRepository.findAllById(anyList()))
            .thenReturn(Collections.emptyList());

        List<ConsultaPacienteResposta> resultado = consultaPacienteService.buscarConsultasAtivas(PACIENTE_ID);

        assertEquals(1, resultado.size());
        assertEquals("Unidade não encontrada", resultado.get(0).getUnidadeNome());
    }
}
