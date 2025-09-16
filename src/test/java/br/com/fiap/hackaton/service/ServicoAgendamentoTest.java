package br.com.fiap.hackaton.service;

import br.com.fiap.hackaton.dto.AgendamentoRequisicao;
import br.com.fiap.hackaton.dto.CancelamentoResposta;
import br.com.fiap.hackaton.dto.SugestaoResposta;
import br.com.fiap.hackaton.dto.TriagemDTO;
import br.com.fiap.hackaton.entity.*;
import br.com.fiap.hackaton.enums.StatusAgendamento;
import br.com.fiap.hackaton.enums.TipoAgendamento;
import br.com.fiap.hackaton.model.Agendamento;
import br.com.fiap.hackaton.repository.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ServicoAgendamentoTest {

    @Mock
    private PacienteRepository pacienteRepository;
    @Mock
    private UnidadeRepository unidadeRepository;
    @Mock
    private AgendamentoRepository agendamentoRepository;
    @Mock
    private HorarioDisponivelRepository horarioDisponivelRepository;
    @Mock
    private TriagemRepository triagemRepository;

    @InjectMocks
    private ServicoAgendamento service;

    private AutoCloseable closeable;

    @BeforeEach
    void init() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        if (closeable != null) closeable.close();
    }

    private PacienteEntity pacienteAtivo() {
        return new PacienteEntity("p1", "Paciente 1", true);
    }

    private UnidadeEntity unidadeComHorarios(String id, String nome, String local, LocalDateTime... datas) {
        UnidadeEntity u = new UnidadeEntity(id, nome, local);
        for (LocalDateTime d : datas) {
            HorarioDisponivelEntity h = new HorarioDisponivelEntity(d, "prof1");
            u.adicionarHorario(h);
        }
        return u;
    }

    @Test
    void buscarSugestao() {
        AgendamentoRequisicao req = new AgendamentoRequisicao();
        req.setPacienteId("p1");
        req.setTipo(TipoAgendamento.CLINICO_GERAL);
        req.setLocalizacao("Centro");
        req.setUrgencia(3);

        when(pacienteRepository.findById("p1")).thenReturn(Optional.of(pacienteAtivo()));
        LocalDateTime d1 = LocalDateTime.now().plusDays(2);
        LocalDateTime d0 = LocalDateTime.now().plusDays(1);
        UnidadeEntity u = unidadeComHorarios("u1", "Unidade Centro", "Centro", d1, d0);
        when(unidadeRepository.buscarPorTipoELocalizacao(TipoAgendamento.CLINICO_GERAL, "Centro"))
                .thenReturn(List.of(u));

        SugestaoResposta s = service.buscarSugestao(req);
        assertNotNull(s);
        assertEquals("u1", s.getUnidadeId());
        assertEquals("prof1", s.getProfissionalId());
        assertEquals(d0.withNano(0), s.getDataHora().withNano(0));
    }

    @Test
    void confirmarAgendamento() {
        String pacienteId = "p1";
        String unidadeId = "u1";
        LocalDateTime data = LocalDateTime.now().plusDays(1);
        String prof = "prof1";
        TipoAgendamento tipo = TipoAgendamento.CLINICO_GERAL;

        when(pacienteRepository.findById(pacienteId)).thenReturn(Optional.of(pacienteAtivo()));
        when(agendamentoRepository.existePorPacienteIdEDataHoraEStatusDiferenteDe(pacienteId, data, StatusAgendamento.CANCELADA))
                .thenReturn(false);
        when(horarioDisponivelRepository.deletarPorUnidadeDataHoraEProfissional(unidadeId, data, prof))
                .thenReturn(1);

        AgendamentoEntity salvo = new AgendamentoEntity("a1", pacienteId, unidadeId, prof, tipo, data, StatusAgendamento.AGENDADA);
        when(agendamentoRepository.save(any(AgendamentoEntity.class))).thenReturn(salvo);

        Agendamento a = service.confirmarAgendamento(pacienteId, unidadeId, data, prof, tipo);
        assertNotNull(a);
        assertEquals("a1", a.getId());
        assertEquals(unidadeId, a.getUnidadeId());
        assertEquals(StatusAgendamento.AGENDADA, a.getStatus());
    }

    @Test
    void buscarSugestaoComTriagem() {
        AgendamentoRequisicao req = new AgendamentoRequisicao();
        req.setPacienteId("p1");
        req.setTipo(TipoAgendamento.EXAME_SANGUE);
        req.setLocalizacao("Centro");
        TriagemDTO tri = new TriagemDTO();
        tri.setPacienteId("p1");
        tri.setUrgencia(5);
        tri.setSintomas("sintomas");
        tri.setMotivo("motivo");

        when(pacienteRepository.findById("p1")).thenReturn(Optional.of(pacienteAtivo()));
        when(triagemRepository.save(any(TriagemEntity.class)))
                .thenAnswer(inv -> {
                    TriagemEntity t = inv.getArgument(0);
                    t.setId("t1");
                    return t;
                });
        LocalDateTime d0 = LocalDateTime.now().plusDays(1);
        UnidadeEntity u = unidadeComHorarios("u1", "Unidade Centro", "Centro", d0);
        when(unidadeRepository.buscarPorTipoELocalizacao(TipoAgendamento.EXAME_SANGUE, "Centro"))
                .thenReturn(List.of(u));

        SugestaoResposta s = service.buscarSugestaoComTriagem(req, tri);
        assertNotNull(s);
        assertEquals("u1", s.getUnidadeId());
        assertEquals("prof1", s.getProfissionalId());
    }

    @Test
    void confirmarAgendamentoComTriagem() {
        String pacienteId = "p1";
        String unidadeId = "u1";
        LocalDateTime data = LocalDateTime.now().plusDays(1);
        String prof = "prof1";
        TipoAgendamento tipo = TipoAgendamento.CLINICO_GERAL;
        String triagemId = "t1";

        when(pacienteRepository.findById(pacienteId)).thenReturn(Optional.of(pacienteAtivo()));
        when(agendamentoRepository.existePorPacienteIdEDataHoraEStatusDiferenteDe(pacienteId, data, StatusAgendamento.CANCELADA))
                .thenReturn(false);
        when(horarioDisponivelRepository.deletarPorUnidadeDataHoraEProfissional(unidadeId, data, prof))
                .thenReturn(1);
        when(triagemRepository.findById(triagemId)).thenReturn(Optional.of(new TriagemEntity(pacienteId, "s", 3, "m")));

        AgendamentoEntity salvo = new AgendamentoEntity("a2", pacienteId, unidadeId, prof, tipo, data, StatusAgendamento.AGENDADA);
        when(agendamentoRepository.save(any(AgendamentoEntity.class))).thenReturn(salvo);

        Agendamento a = service.confirmarAgendamentoComTriagem(pacienteId, unidadeId, data, prof, tipo, triagemId);
        assertNotNull(a);
        assertEquals("a2", a.getId());
        assertEquals(StatusAgendamento.AGENDADA, a.getStatus());
    }

    @Test
    void reagendarAgendamento() {
        String agendamentoId = "a1";
        AgendamentoEntity existente = new AgendamentoEntity(agendamentoId, "p1", "u0", "prof0", TipoAgendamento.CLINICO_GERAL, LocalDateTime.now().plusDays(2), StatusAgendamento.AGENDADA);
        when(agendamentoRepository.findById(agendamentoId)).thenReturn(Optional.of(existente));

        LocalDateTime novaData = LocalDateTime.now().plusDays(5);
        when(agendamentoRepository.save(any(AgendamentoEntity.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        Agendamento a = service.reagendarAgendamento(agendamentoId, "u1", novaData, "prof1");
        assertNotNull(a);
        assertEquals("u1", a.getUnidadeId());
        assertEquals("prof1", a.getProfissionalId());
        assertEquals(StatusAgendamento.REAGENDADA, a.getStatus());
    }

    @Test
    void confirmar() {
        String pacienteId = "p1";
        String unidadeId = "u1";
        LocalDateTime data = LocalDateTime.now().plusDays(1);
        String prof = "prof1";
        TipoAgendamento tipo = TipoAgendamento.CLINICO_GERAL;

        when(pacienteRepository.findById(pacienteId)).thenReturn(Optional.of(pacienteAtivo()));
        when(agendamentoRepository.existePorPacienteIdEDataHoraEStatusDiferenteDe(pacienteId, data, StatusAgendamento.CANCELADA))
                .thenReturn(false);
        when(horarioDisponivelRepository.deletarPorUnidadeDataHoraEProfissional(unidadeId, data, prof))
                .thenReturn(1);

        AgendamentoEntity salvo = new AgendamentoEntity("a3", pacienteId, unidadeId, prof, tipo, data, StatusAgendamento.AGENDADA);
        when(agendamentoRepository.save(any(AgendamentoEntity.class))).thenReturn(salvo);

        Agendamento a = service.confirmar(pacienteId, unidadeId, data, prof, tipo);
        assertNotNull(a);
        assertEquals("a3", a.getId());
    }

    @Test
    void confirmarComTriagem() {
        String pacienteId = "p1";
        String unidadeId = "u1";
        LocalDateTime data = LocalDateTime.now().plusDays(1);
        String prof = "prof1";
        TipoAgendamento tipo = TipoAgendamento.CLINICO_GERAL;
        String triagemId = "t1";

        when(pacienteRepository.findById(pacienteId)).thenReturn(Optional.of(pacienteAtivo()));
        when(agendamentoRepository.existePorPacienteIdEDataHoraEStatusDiferenteDe(pacienteId, data, StatusAgendamento.CANCELADA))
                .thenReturn(false);
        when(horarioDisponivelRepository.deletarPorUnidadeDataHoraEProfissional(unidadeId, data, prof))
                .thenReturn(1);
        when(triagemRepository.findById(triagemId)).thenReturn(Optional.of(new TriagemEntity(pacienteId, "s", 3, "m")));

        AgendamentoEntity salvo = new AgendamentoEntity("a4", pacienteId, unidadeId, prof, tipo, data, StatusAgendamento.AGENDADA);
        when(agendamentoRepository.save(any(AgendamentoEntity.class))).thenReturn(salvo);

        Agendamento a = service.confirmarComTriagem(pacienteId, unidadeId, data, prof, tipo, triagemId);
        assertNotNull(a);
        assertEquals("a4", a.getId());
    }

    @Test
    void sugerir() {
        AgendamentoRequisicao req = new AgendamentoRequisicao();
        req.setPacienteId("p1");
        req.setTipo(TipoAgendamento.CLINICO_GERAL);
        req.setLocalizacao("Centro");
        req.setUrgencia(2);

        when(pacienteRepository.findById("p1")).thenReturn(Optional.of(pacienteAtivo()));
        LocalDateTime d0 = LocalDateTime.now().plusDays(1);
        UnidadeEntity u = unidadeComHorarios("u1", "Unidade Centro", "Centro", d0);
        when(unidadeRepository.buscarPorTipoELocalizacao(TipoAgendamento.CLINICO_GERAL, "Centro"))
                .thenReturn(List.of(u));

        SugestaoResposta s = service.sugerir(req);
        assertNotNull(s);
        assertEquals("u1", s.getUnidadeId());
    }

    @Test
    void sugerirComTriagem() {
        AgendamentoRequisicao req = new AgendamentoRequisicao();
        req.setPacienteId("p1");
        req.setTipo(TipoAgendamento.CLINICO_GERAL);
        req.setLocalizacao("Centro");
        TriagemDTO tri = new TriagemDTO();
        tri.setPacienteId("p1");
        tri.setUrgencia(4);

        when(pacienteRepository.findById("p1")).thenReturn(Optional.of(pacienteAtivo()));
        when(triagemRepository.save(any(TriagemEntity.class)))
                .thenAnswer(inv -> inv.getArgument(0));
        LocalDateTime d0 = LocalDateTime.now().plusDays(1);
        UnidadeEntity u = unidadeComHorarios("u1", "Unidade Centro", "Centro", d0);
        when(unidadeRepository.buscarPorTipoELocalizacao(TipoAgendamento.CLINICO_GERAL, "Centro"))
                .thenReturn(List.of(u));

        SugestaoResposta s = service.sugerirComTriagem(req, tri);
        assertNotNull(s);
        assertEquals("u1", s.getUnidadeId());
    }

    @Test
    void reagendar() {
        String agendamentoId = "a9";
        AgendamentoEntity existente = new AgendamentoEntity(agendamentoId, "p1", "u0", "prof0", TipoAgendamento.CLINICO_GERAL, LocalDateTime.now().plusDays(2), StatusAgendamento.AGENDADA);
        when(agendamentoRepository.findById(agendamentoId)).thenReturn(Optional.of(existente));

        LocalDateTime novaData = LocalDateTime.now().plusDays(10);
        when(agendamentoRepository.save(any(AgendamentoEntity.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        Agendamento a = service.reagendar(agendamentoId, "u2", novaData, "prof2");
        assertNotNull(a);
        assertEquals("u2", a.getUnidadeId());
        assertEquals("prof2", a.getProfissionalId());
        assertEquals(StatusAgendamento.REAGENDADA, a.getStatus());
    }

    @Test
    void cancelarAgendamento_QuandoDadosValidos_DeveCancelarComSucesso() {
        // Arrange
        String agendamentoId = "a1";
        String pacienteId = "p1";
        String unidadeId = "u1";
        String profissionalId = "prof1";
        LocalDateTime dataHora = LocalDateTime.now().plusDays(2); // Mais de 24h no futuro

        AgendamentoEntity agendamento = new AgendamentoEntity(
                agendamentoId,
                pacienteId,
                unidadeId,
                profissionalId,
                TipoAgendamento.CLINICO_GERAL,
                dataHora,
                StatusAgendamento.AGENDADA
        );

        UnidadeEntity unidade = new UnidadeEntity(unidadeId, "Unidade Teste", "Centro");

        when(agendamentoRepository.findById(agendamentoId)).thenReturn(Optional.of(agendamento));
        when(unidadeRepository.findById(unidadeId)).thenReturn(Optional.of(unidade));
        when(agendamentoRepository.save(any(AgendamentoEntity.class))).thenReturn(agendamento);
        when(horarioDisponivelRepository.save(any(HorarioDisponivelEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        CancelamentoResposta resposta = service.cancelarAgendamento(agendamentoId, pacienteId);

        // Assert
        assertNotNull(resposta);
        assertEquals(agendamentoId, resposta.getAgendamentoId());
        assertEquals(pacienteId, resposta.getPacienteId());
        assertEquals(StatusAgendamento.CANCELADA, resposta.getStatus());

        verify(agendamentoRepository).findById(agendamentoId);
        verify(unidadeRepository).findById(unidadeId);
        verify(agendamentoRepository).save(any(AgendamentoEntity.class));
        verify(horarioDisponivelRepository).save(any(HorarioDisponivelEntity.class));
    }

    @Test
    void cancelarAgendamento_QuandoAgendamentoNaoExiste_DeveLancarExcecao() {
        // Arrange
        String agendamentoId = "id_inexistente";
        String pacienteId = "p1";

        when(agendamentoRepository.findById(agendamentoId)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                service.cancelarAgendamento(agendamentoId, pacienteId)
        );

        assertEquals("Agendamento não encontrado", exception.getMessage());
        verify(agendamentoRepository).findById(agendamentoId);
        verifyNoMoreInteractions(agendamentoRepository, unidadeRepository, horarioDisponivelRepository);
    }

    @Test
    void cancelarAgendamento_QuandoPacienteNaoCorresponde_DeveLancarExcecao() {
        // Arrange
        String agendamentoId = "a1";
        String pacienteIdCorreto = "p1";
        String pacienteIdIncorreto = "p2";

        AgendamentoEntity agendamento = new AgendamentoEntity(
                agendamentoId,
                pacienteIdCorreto,
                "u1",
                "prof1",
                TipoAgendamento.CLINICO_GERAL,
                LocalDateTime.now().plusDays(2),
                StatusAgendamento.AGENDADA
        );

        when(agendamentoRepository.findById(agendamentoId)).thenReturn(Optional.of(agendamento));

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                service.cancelarAgendamento(agendamentoId, pacienteIdIncorreto)
        );

        assertEquals("Este agendamento não pertence ao paciente informado", exception.getMessage());
        verify(agendamentoRepository).findById(agendamentoId);
        verifyNoMoreInteractions(agendamentoRepository, unidadeRepository, horarioDisponivelRepository);
    }

    @Test
    void cancelarAgendamento_QuandoJaCancelado_DeveLancarExcecao() {
        // Arrange
        String agendamentoId = "a1";
        String pacienteId = "p1";

        AgendamentoEntity agendamento = new AgendamentoEntity(
                agendamentoId,
                pacienteId,
                "u1",
                "prof1",
                TipoAgendamento.CLINICO_GERAL,
                LocalDateTime.now().plusDays(2),
                StatusAgendamento.CANCELADA // Já está cancelado
        );

        when(agendamentoRepository.findById(agendamentoId)).thenReturn(Optional.of(agendamento));

        // Act & Assert
        IllegalStateException exception = assertThrows(IllegalStateException.class, () ->
                service.cancelarAgendamento(agendamentoId, pacienteId)
        );

        assertEquals("Agendamento já está cancelado", exception.getMessage());
        verify(agendamentoRepository).findById(agendamentoId);
        verifyNoMoreInteractions(agendamentoRepository, unidadeRepository, horarioDisponivelRepository);
    }

    @Test
    void cancelarAgendamento_QuandoPrazoExpirado_DeveLancarExcecao() {
        // Arrange
        String agendamentoId = "a1";
        String pacienteId = "p1";

        // Data menos de 24h no futuro (prazo expirado)
        LocalDateTime dataProxima = LocalDateTime.now().plusHours(23);

        AgendamentoEntity agendamento = new AgendamentoEntity(
                agendamentoId,
                pacienteId,
                "u1",
                "prof1",
                TipoAgendamento.CLINICO_GERAL,
                dataProxima,
                StatusAgendamento.AGENDADA
        );

        when(agendamentoRepository.findById(agendamentoId)).thenReturn(Optional.of(agendamento));

        // Act & Assert
        IllegalStateException exception = assertThrows(IllegalStateException.class, () ->
                service.cancelarAgendamento(agendamentoId, pacienteId)
        );

        assertTrue(exception.getMessage().contains("Não é possível cancelar agendamentos com menos de"));
        verify(agendamentoRepository).findById(agendamentoId);
        verifyNoMoreInteractions(agendamentoRepository, unidadeRepository, horarioDisponivelRepository);
    }

    @Test
    void cancelarAgendamento_QuandoUnidadeNaoEncontrada_DeveLancarExcecao() {
        // Arrange
        String agendamentoId = "a1";
        String pacienteId = "p1";
        String unidadeId = "u1";

        AgendamentoEntity agendamento = new AgendamentoEntity(
                agendamentoId,
                pacienteId,
                unidadeId,
                "prof1",
                TipoAgendamento.CLINICO_GERAL,
                LocalDateTime.now().plusDays(2),
                StatusAgendamento.AGENDADA
        );

        when(agendamentoRepository.findById(agendamentoId)).thenReturn(Optional.of(agendamento));
        when(agendamentoRepository.save(any(AgendamentoEntity.class))).thenReturn(agendamento);
        when(unidadeRepository.findById(unidadeId)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalStateException exception = assertThrows(IllegalStateException.class, () ->
                service.cancelarAgendamento(agendamentoId, pacienteId)
        );

        assertEquals("Unidade não encontrada", exception.getMessage());
        verify(agendamentoRepository).findById(agendamentoId);
        verify(agendamentoRepository).save(any(AgendamentoEntity.class));
        verify(unidadeRepository).findById(unidadeId);
    }
}