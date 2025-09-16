package br.com.fiap.jackaton.controller;

import br.com.fiap.jackaton.dto.AgendamentoRequisicao;
import br.com.fiap.jackaton.dto.ConfirmacaoRequisicao;
import br.com.fiap.jackaton.dto.SugestaoResposta;
import br.com.fiap.jackaton.dto.TriagemRequisicaoDTO;
import br.com.fiap.jackaton.dto.RecusaRequisicao;
import br.com.fiap.jackaton.dto.RecusaResposta;
import br.com.fiap.jackaton.enums.TipoAgendamento;
import br.com.fiap.jackaton.model.Agendamento;
import br.com.fiap.jackaton.service.ServicoAgendamento;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/agendamentos")
public class AgendamentoController {

    private final ServicoAgendamento servico;

    public AgendamentoController(ServicoAgendamento servico) {
        this.servico = servico;
    }

    @PostMapping("/confirmar")
    public ResponseEntity<Agendamento> confirmar(@Valid @RequestBody ConfirmacaoRequisicao requisicao) {
        Agendamento agendamento = servico.confirmar(requisicao.getPacienteId(), requisicao.getUnidadeId(),
                                              requisicao.getDataHora(), requisicao.getProfissionalId(),
                                              requisicao.getTipo());
        return ResponseEntity.status(HttpStatus.CREATED)
                .header("X-mensagem", "Agendamento confirmado")
                .body(agendamento);
    }

    @PostMapping("/confirmar-com-triagem")
    public ResponseEntity<Agendamento> confirmarComTriagem(@RequestParam String pacienteId,
                                                            @RequestParam String unidadeId,
                                                            @RequestParam String dataHora,
                                                            @RequestParam String profissionalId,
                                                            @RequestParam TipoAgendamento tipo,
                                                            @RequestParam String triagemId) {
        Agendamento agendamento = servico.confirmarComTriagem(pacienteId, unidadeId,
                LocalDateTime.parse(dataHora),
                profissionalId, tipo, triagemId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .header("X-mensagem", "Agendamento confirmado com triagem")
                .body(agendamento);
    }

    @PostMapping("/sugerir")
    public ResponseEntity<SugestaoResposta> sugerir(@Valid @RequestBody AgendamentoRequisicao requisicao) {
        SugestaoResposta sugestao = servico.sugerir(requisicao);
        return ResponseEntity.ok()
                .header("X-mensagem", "Sugestão gerada")
                .body(sugestao);
    }

    @PostMapping("/sugerir-com-triagem")
    public ResponseEntity<SugestaoResposta> sugerirComTriagem(@Valid @RequestBody TriagemRequisicaoDTO triagemRequisicao) {
        SugestaoResposta sugestao = servico.sugerirComTriagem(triagemRequisicao.getRequisicao(), triagemRequisicao.getTriagem());
        return ResponseEntity.ok()
                .header("X-mensagem", "Sugestão com triagem gerada")
                .body(sugestao);
    }

    @PostMapping("/reagendar")
    public ResponseEntity<Agendamento> reagendar(@RequestParam String agendamentoId,
                                          @RequestParam String novaUnidadeId,
                                          @RequestParam String novaDataHora,
                                          @RequestParam String novoProfissionalId) {
        Agendamento agendamento = servico.reagendar(agendamentoId, novaUnidadeId,
                                            LocalDateTime.parse(novaDataHora), novoProfissionalId);
        return ResponseEntity.ok()
                .header("X-mensagem", "Agendamento reagendado")
                .body(agendamento);
    }

    @PostMapping("/recusar")
    public ResponseEntity<RecusaResposta> recusarSugestao(@Valid @RequestBody RecusaRequisicao requisicao) {
        RecusaResposta resposta = servico.registrarRecusa(requisicao);
        return ResponseEntity.ok()
                .header("X-mensagem", resposta.getMensagem())
                .body(resposta);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Object> tratarConflito(IllegalStateException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("erro", ex.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> tratarRequisicaoInvalida(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("erro", ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<String>> tratarErrosValidacao(MethodArgumentNotValidException ex) {
        List<String> erros = ex.getBindingResult().getFieldErrors().stream()
                .map(err -> err.getField() + ": " + (err.getDefaultMessage() != null ? err.getDefaultMessage() : err.getCode()))
                .collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erros);
    }
}
