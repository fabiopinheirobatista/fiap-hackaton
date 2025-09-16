package br.com.fiap.jackaton.controller;

import br.com.fiap.jackaton.dto.AgendaRequest;
import br.com.fiap.jackaton.dto.AgendaResponse;
import br.com.fiap.jackaton.dto.BloqueioRequest;
import br.com.fiap.jackaton.service.ServicoAgenda;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/agenda")
public class AgendaController {

    private final ServicoAgenda servicoAgenda;

    public AgendaController(ServicoAgenda servicoAgenda) {
        this.servicoAgenda = servicoAgenda;
    }

    @GetMapping("/visualizar")
    public ResponseEntity<AgendaResponse> visualizarAgenda(@Valid @RequestBody AgendaRequest requisicao) {
        AgendaResponse agenda = servicoAgenda.visualizarAgenda(requisicao);
        return ResponseEntity.ok()
                .header("X-mensagem", "Agenda recuperada com sucesso")
                .body(agenda);
    }

    @PostMapping("/bloquear")
    public ResponseEntity<Map<String, String>> criarBloqueio(@Valid @RequestBody BloqueioRequest requisicao) {
        servicoAgenda.criarBloqueio(requisicao);
        return ResponseEntity.status(HttpStatus.CREATED)
                .header("X-mensagem", "Bloqueio criado com sucesso")
                .body(Map.of("mensagem", "Horário bloqueado com sucesso"));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> tratarRequisicaoInvalida(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("erro", ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<String>> tratarErrosValidacao(MethodArgumentNotValidException ex) {
        List<String> erros = ex.getBindingResult().getFieldErrors().stream()
                .map(err -> err.getField() + ": " + (err.getDefaultMessage() != null ? err.getDefaultMessage() : err.getCode()))
                .collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erros);
    }
}
