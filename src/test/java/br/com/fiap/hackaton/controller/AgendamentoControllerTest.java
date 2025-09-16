package br.com.fiap.hackaton.controller;

import br.com.fiap.hackaton.dto.*;
import br.com.fiap.hackaton.enums.StatusAgendamento;
import br.com.fiap.hackaton.enums.TipoAgendamento;
import br.com.fiap.hackaton.model.Agendamento;
import br.com.fiap.hackaton.service.ServicoAgendamento;
import jakarta.validation.Valid;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class AgendamentoControllerTest {

    @Mock
    private ServicoAgendamento servico;

    @InjectMocks
    private AgendamentoController controller;

    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        if (closeable != null) closeable.close();
    }

    @Test
    void confirmar_DeveRetornarAgendamentoConfirmado() {
        ConfirmacaoRequisicao requisicao = new ConfirmacaoRequisicao();
        requisicao.setPacienteId("123");
        requisicao.setUnidadeId("456");
        requisicao.setDataHora(LocalDateTime.now());
        requisicao.setProfissionalId("789");
        requisicao.setTipo(TipoAgendamento.CLINICO_GERAL);

        Agendamento agendamentoEsperado = new Agendamento(
            "1", "123", "456", "789",
            TipoAgendamento.CLINICO_GERAL, LocalDateTime.now(),
            StatusAgendamento.AGENDADA
        );

        when(servico.confirmar(
            anyString(), anyString(), any(LocalDateTime.class),
            anyString(), any(TipoAgendamento.class)
        )).thenReturn(agendamentoEsperado);

        ResponseEntity<Agendamento> resposta = controller.confirmar(requisicao);

        assertNotNull(resposta);
        assertEquals(HttpStatus.CREATED, resposta.getStatusCode());
        assertNotNull(resposta.getBody());
        assertEquals(agendamentoEsperado.getId(), resposta.getBody().getId());
    }

    @Test
    void confirmarComTriagem_DeveRetornarAgendamentoConfirmado() {
        String pacienteId = "123";
        String unidadeId = "456";
        String dataHora = LocalDateTime.now().toString();
        String profissionalId = "789";
        TipoAgendamento tipo = TipoAgendamento.CLINICO_GERAL;
        String triagemId = "999";

        Agendamento agendamentoEsperado = new Agendamento(
            "1", pacienteId, unidadeId, profissionalId,
            tipo, LocalDateTime.parse(dataHora),
            StatusAgendamento.AGENDADA
        );

        when(servico.confirmarComTriagem(
            anyString(), anyString(), any(LocalDateTime.class),
            anyString(), any(TipoAgendamento.class), anyString()
        )).thenReturn(agendamentoEsperado);

        ResponseEntity<Agendamento> resposta = controller.confirmarComTriagem(
            pacienteId, unidadeId, dataHora, profissionalId, tipo, triagemId
        );

        assertNotNull(resposta);
        assertEquals(HttpStatus.CREATED, resposta.getStatusCode());
        assertNotNull(resposta.getBody());
        assertEquals(agendamentoEsperado.getId(), resposta.getBody().getId());
    }

    @Test
    void sugerir_DeveRetornarSugestaoValida() {
        AgendamentoRequisicao requisicao = new AgendamentoRequisicao();
        requisicao.setPacienteId("123");
        requisicao.setTipo(TipoAgendamento.CLINICO_GERAL);
        requisicao.setLocalizacao("SP");
        requisicao.setUrgencia(3);

        SugestaoResposta sugestaoEsperada = new SugestaoResposta(
            "456", "Unidade SP", LocalDateTime.now(), "789"
        );

        when(servico.sugerir(any(AgendamentoRequisicao.class)))
            .thenReturn(sugestaoEsperada);

        ResponseEntity<SugestaoResposta> resposta = controller.sugerir(requisicao);

        assertNotNull(resposta);
        assertEquals(HttpStatus.OK, resposta.getStatusCode());
        assertNotNull(resposta.getBody());
        assertEquals(sugestaoEsperada.getUnidadeId(), resposta.getBody().getUnidadeId());
    }

    @Test
    void sugerirComTriagem_DeveRetornarSugestaoValida() {
        AgendamentoRequisicao req = new AgendamentoRequisicao();
        req.setPacienteId("123");
        req.setTipo(TipoAgendamento.CLINICO_GERAL);
        req.setLocalizacao("SP");
        req.setUrgencia(4);

        TriagemDTO triagem = new TriagemDTO();
        triagem.setPacienteId("123");
        triagem.setSintomas("Febre, Dor de cabeça");
        triagem.setUrgencia(4);
        triagem.setMotivo("Emergência");

        TriagemRequisicaoDTO triagemRequisicao = new TriagemRequisicaoDTO();
        triagemRequisicao.setRequisicao(req);
        triagemRequisicao.setTriagem(triagem);

        SugestaoResposta sugestaoEsperada = new SugestaoResposta(
            "456", "Unidade SP", LocalDateTime.now(), "789"
        );

        when(servico.sugerirComTriagem(any(AgendamentoRequisicao.class), any(TriagemDTO.class)))
            .thenReturn(sugestaoEsperada);

        ResponseEntity<SugestaoResposta> resposta = controller.sugerirComTriagem(triagemRequisicao);

        assertNotNull(resposta);
        assertEquals(HttpStatus.OK, resposta.getStatusCode());
        assertNotNull(resposta.getBody());
        assertEquals(sugestaoEsperada.getUnidadeId(), resposta.getBody().getUnidadeId());
    }

    @Test
    void reagendar_DeveRetornarAgendamentoReagendado() {
        String agendamentoId = "123";
        String novaUnidadeId = "456";
        String novaDataHora = LocalDateTime.now().toString();
        String novoProfissionalId = "789";

        Agendamento agendamentoEsperado = new Agendamento(
            agendamentoId, "paciente123", novaUnidadeId, novoProfissionalId,
            TipoAgendamento.CLINICO_GERAL, LocalDateTime.parse(novaDataHora),
            StatusAgendamento.REAGENDADA
        );

        when(servico.reagendar(
            anyString(), anyString(), any(LocalDateTime.class), anyString()
        )).thenReturn(agendamentoEsperado);

        ResponseEntity<Agendamento> resposta = controller.reagendar(
            agendamentoId, novaUnidadeId, novaDataHora, novoProfissionalId
        );

        assertNotNull(resposta);
        assertEquals(HttpStatus.OK, resposta.getStatusCode());
        assertNotNull(resposta.getBody());
        assertEquals(StatusAgendamento.REAGENDADA, resposta.getBody().getStatus());
    }

    @Test
    void tratarConflito_DeveRetornarStatusConflict() {
        IllegalStateException ex = new IllegalStateException("Conflito de horário");

        ResponseEntity<Object> resposta = controller.tratarConflito(ex);

        assertEquals(HttpStatus.CONFLICT, resposta.getStatusCode());
        assertInstanceOf(Map.class, resposta.getBody());
        assertEquals("Conflito de horário", ((Map<?, ?>) resposta.getBody()).get("erro"));
    }

    @Test
    void tratarRequisicaoInvalida_DeveRetornarStatusBadRequest() {
        IllegalArgumentException ex = new IllegalArgumentException("Dados inválidos");

        ResponseEntity<Object> resposta = controller.tratarRequisicaoInvalida(ex);

        assertEquals(HttpStatus.BAD_REQUEST, resposta.getStatusCode());
        assertInstanceOf(Map.class, resposta.getBody());
        assertEquals("Dados inválidos", ((Map<?, ?>) resposta.getBody()).get("erro"));
    }

    @Test
    void tratarErrosValidacao_DeveRetornarListaDeErros() throws Exception {
        AgendamentoRequisicao alvo = new AgendamentoRequisicao();
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(alvo, "agendamentoRequisicao");
        bindingResult.addError(new FieldError("agendamentoRequisicao", "campo1", "Erro 1"));
        bindingResult.addError(new FieldError("agendamentoRequisicao", "campo2", "Erro 2"));

        Method m = this.getClass().getDeclaredMethod("dummy", AgendamentoRequisicao.class);
        MethodParameter parameter = new MethodParameter(m, 0);
        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(parameter, bindingResult);

        ResponseEntity<List<String>> resposta = controller.tratarErrosValidacao(ex);

        assertNotNull(resposta);
        assertEquals(HttpStatus.BAD_REQUEST, resposta.getStatusCode());
        assertNotNull(resposta.getBody());
        assertEquals(2, resposta.getBody().size());
        assertTrue(resposta.getBody().stream().anyMatch(s -> s.contains("campo1") && s.contains("Erro 1")));
        assertTrue(resposta.getBody().stream().anyMatch(s -> s.contains("campo2") && s.contains("Erro 2")));

        dummy(alvo);
    }

    private void dummy(@Valid AgendamentoRequisicao req) {
        Objects.requireNonNull(req);
    }

    @Test
    void cancelar_QuandoDadosValidos_DeveRetornarRespostaDeCancelamento() {
        // Arrange
        CancelamentoRequisicao requisicao = new CancelamentoRequisicao("a1", "p1", "Motivo do cancelamento");

        CancelamentoResposta respostaEsperada = new CancelamentoResposta(
                "a1",
                "p1",
                LocalDateTime.now().plusDays(2),
                StatusAgendamento.CANCELADA,
                "Agendamento cancelado com sucesso"
        );

        when(servico.cancelarAgendamento(requisicao.getAgendamentoId(), requisicao.getPacienteId()))
                .thenReturn(respostaEsperada);

        // Act
        ResponseEntity<CancelamentoResposta> resposta = controller.cancelar(requisicao);

        // Assert
        assertNotNull(resposta);
        assertEquals(HttpStatus.OK, resposta.getStatusCode());
        assertEquals("Agendamento cancelado com sucesso", resposta.getHeaders().getFirst("X-mensagem"));
        assertEquals(respostaEsperada, resposta.getBody());

        verify(servico).cancelarAgendamento(requisicao.getAgendamentoId(), requisicao.getPacienteId());
    }

    @Test
    void cancelar_QuandoAgendamentoNaoExiste_DeveRetornarErro() {
        // Arrange
        CancelamentoRequisicao requisicao = new CancelamentoRequisicao("id_inexistente", "p1", "Motivo do cancelamento");

        when(servico.cancelarAgendamento(anyString(), anyString()))
                .thenThrow(new IllegalArgumentException("Agendamento não encontrado"));

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
            controller.cancelar(requisicao)
        );

        assertEquals("Agendamento não encontrado", exception.getMessage());
        verify(servico).cancelarAgendamento(requisicao.getAgendamentoId(), requisicao.getPacienteId());
    }

    @Test
    void cancelar_QuandoPacienteNaoCorresponde_DeveRetornarErro() {
        // Arrange
        CancelamentoRequisicao requisicao = new CancelamentoRequisicao("a1", "p2", "Motivo do cancelamento");

        when(servico.cancelarAgendamento(anyString(), anyString()))
                .thenThrow(new IllegalArgumentException("Este agendamento não pertence ao paciente informado"));

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
            controller.cancelar(requisicao)
        );

        assertEquals("Este agendamento não pertence ao paciente informado", exception.getMessage());
        verify(servico).cancelarAgendamento(requisicao.getAgendamentoId(), requisicao.getPacienteId());
    }

    @Test
    void cancelar_QuandoAgendamentoJaEstaCancelado_DeveRetornarErro() {
        // Arrange
        CancelamentoRequisicao requisicao = new CancelamentoRequisicao("a1", "p1", "Motivo do cancelamento");

        when(servico.cancelarAgendamento(anyString(), anyString()))
                .thenThrow(new IllegalStateException("Agendamento já está cancelado"));

        // Act & Assert
        Exception exception = assertThrows(IllegalStateException.class, () ->
            controller.cancelar(requisicao)
        );

        assertEquals("Agendamento já está cancelado", exception.getMessage());
        verify(servico).cancelarAgendamento(requisicao.getAgendamentoId(), requisicao.getPacienteId());
    }

    @Test
    void cancelar_QuandoPrazoExpirado_DeveRetornarErro() {
        // Arrange
        CancelamentoRequisicao requisicao = new CancelamentoRequisicao("a1", "p1", "Motivo do cancelamento");

        when(servico.cancelarAgendamento(anyString(), anyString()))
                .thenThrow(new IllegalStateException("Não é possível cancelar agendamentos com menos de 24 horas de antecedência"));

        // Act & Assert
        Exception exception = assertThrows(IllegalStateException.class, () ->
            controller.cancelar(requisicao)
        );

        assertTrue(exception.getMessage().contains("Não é possível cancelar agendamentos com menos de"));
        verify(servico).cancelarAgendamento(requisicao.getAgendamentoId(), requisicao.getPacienteId());
    }
}