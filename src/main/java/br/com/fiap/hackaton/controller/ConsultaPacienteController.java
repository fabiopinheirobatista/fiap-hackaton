package br.com.fiap.hackaton.controller;

import br.com.fiap.hackaton.dto.ConsultaPacienteResposta;
import br.com.fiap.hackaton.service.ConsultaPacienteService;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pacientes")
@Validated
public class ConsultaPacienteController {

    private final ConsultaPacienteService consultaPacienteService;

    public ConsultaPacienteController(ConsultaPacienteService consultaPacienteService) {
        this.consultaPacienteService = consultaPacienteService;
    }

    @GetMapping("/{pacienteId}/consultas/ativas")
    public ResponseEntity<List<ConsultaPacienteResposta>> buscarConsultasAtivas(
            @PathVariable
            @NotBlank(message = "ID do paciente é obrigatório")
            @Size(min = 1, max = 50, message = "ID do paciente deve ter entre 1 e 50 caracteres")
            String pacienteId) {

        List<ConsultaPacienteResposta> consultas = consultaPacienteService.buscarConsultasAtivas(pacienteId);
        return ResponseEntity.ok(consultas);
    }

    @GetMapping("/{pacienteId}/consultas/historico")
    public ResponseEntity<List<ConsultaPacienteResposta>> buscarHistoricoConsultas(
            @PathVariable
            @NotBlank(message = "ID do paciente é obrigatório")
            @Size(min = 1, max = 50, message = "ID do paciente deve ter entre 1 e 50 caracteres")
            String pacienteId) {

        List<ConsultaPacienteResposta> consultas = consultaPacienteService.buscarHistoricoConsultas(pacienteId);
        return ResponseEntity.ok(consultas);
    }

    @GetMapping("/{pacienteId}/consultas/todas")
    public ResponseEntity<List<ConsultaPacienteResposta>> buscarTodasConsultas(
            @PathVariable
            @NotBlank(message = "ID do paciente é obrigatório")
            @Size(min = 1, max = 50, message = "ID do paciente deve ter entre 1 e 50 caracteres")
            String pacienteId) {

        List<ConsultaPacienteResposta> consultas = consultaPacienteService.buscarTodasConsultas(pacienteId);
        return ResponseEntity.ok(consultas);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, String>> handleConstraintViolationException(ConstraintViolationException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getConstraintViolations().forEach(violation -> {
            String fieldName = violation.getPropertyPath().toString();
            String errorMessage = violation.getMessage();
            errors.put(fieldName, errorMessage);
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
}
