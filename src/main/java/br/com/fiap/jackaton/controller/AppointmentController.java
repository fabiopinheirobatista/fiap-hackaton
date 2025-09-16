package br.com.fiap.jackaton.controller;

import br.com.fiap.jackaton.dto.AppointmentRequest;
import br.com.fiap.jackaton.dto.ConfirmationRequest;
import br.com.fiap.jackaton.dto.SuggestionResponse;
import br.com.fiap.jackaton.model.Appointment;
import br.com.fiap.jackaton.service.AppointmentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    private final AppointmentService service;

    public AppointmentController(AppointmentService service) {
        this.service = service;
    }

    @PostMapping("/suggest")
    public ResponseEntity<SuggestionResponse> suggest(@Valid @RequestBody AppointmentRequest request) {
        SuggestionResponse suggestion = service.suggest(request);
        return ResponseEntity.ok(suggestion);
    }


    @PostMapping("/confirm")
    public ResponseEntity<Appointment> confirm(@Valid @RequestBody ConfirmationRequest request) {
        Appointment appointment = service.confirm(request.getPatientId(), request.getUnitId(), request.getDateTime(), request.getProfessionalId(), request.getType());
        return ResponseEntity.status(HttpStatus.CREATED).body(appointment);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleBadRequest(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Object> handleConflict(IllegalStateException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<String>> handleValidationErrors(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(err -> err.getField() + ": " + (err.getDefaultMessage() != null ? err.getDefaultMessage() : err.getCode()))
                .collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }
}
