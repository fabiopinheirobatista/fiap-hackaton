package br.com.fiap.hackaton.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CancelamentoRequisicaoTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void quandoTodosOsCamposValidos_NaoDeveRetornarViolacoes() {
        CancelamentoRequisicao requisicao = new CancelamentoRequisicao(
                "agendamento123",
                "paciente456",
                "Não poderei comparecer"
        );

        Set<ConstraintViolation<CancelamentoRequisicao>> violations = validator.validate(requisicao);

        assertTrue(violations.isEmpty());
    }

    @Test
    void quandoAgendamentoIdNulo_DeveRetornarViolacao() {
        CancelamentoRequisicao requisicao = new CancelamentoRequisicao(
                null,
                "paciente456",
                "Não poderei comparecer"
        );

        Set<ConstraintViolation<CancelamentoRequisicao>> violations = validator.validate(requisicao);

        assertEquals(1, violations.size());
        assertEquals("ID do agendamento é obrigatório", violations.iterator().next().getMessage());
    }

    @Test
    void quandoAgendamentoIdVazio_DeveRetornarViolacao() {
        CancelamentoRequisicao requisicao = new CancelamentoRequisicao(
                "",
                "paciente456",
                "Não poderei comparecer"
        );

        Set<ConstraintViolation<CancelamentoRequisicao>> violations = validator.validate(requisicao);

        assertEquals(1, violations.size());
        assertEquals("ID do agendamento é obrigatório", violations.iterator().next().getMessage());
    }

    @Test
    void quandoAgendamentoIdMuitoLongo_DeveRetornarViolacao() {
        String idMuitoLongo = "a".repeat(51); // 51 caracteres, excede o limite de 50

        CancelamentoRequisicao requisicao = new CancelamentoRequisicao(
                idMuitoLongo,
                "paciente456",
                "Não poderei comparecer"
        );

        Set<ConstraintViolation<CancelamentoRequisicao>> violations = validator.validate(requisicao);

        assertEquals(1, violations.size());
        assertEquals("ID do agendamento deve ter no máximo 50 caracteres", violations.iterator().next().getMessage());
    }

    @Test
    void quandoPacienteIdNulo_DeveRetornarViolacao() {
        CancelamentoRequisicao requisicao = new CancelamentoRequisicao(
                "agendamento123",
                null,
                "Não poderei comparecer"
        );

        Set<ConstraintViolation<CancelamentoRequisicao>> violations = validator.validate(requisicao);

        assertEquals(1, violations.size());
        assertEquals("ID do paciente é obrigatório", violations.iterator().next().getMessage());
    }

    @Test
    void quandoPacienteIdVazio_DeveRetornarViolacao() {
        CancelamentoRequisicao requisicao = new CancelamentoRequisicao(
                "agendamento123",
                "",
                "Não poderei comparecer"
        );

        Set<ConstraintViolation<CancelamentoRequisicao>> violations = validator.validate(requisicao);

        assertEquals(1, violations.size());
        assertEquals("ID do paciente é obrigatório", violations.iterator().next().getMessage());
    }

    @Test
    void quandoPacienteIdMuitoLongo_DeveRetornarViolacao() {
        String idMuitoLongo = "p".repeat(51); // 51 caracteres, excede o limite de 50

        CancelamentoRequisicao requisicao = new CancelamentoRequisicao(
                "agendamento123",
                idMuitoLongo,
                "Não poderei comparecer"
        );

        Set<ConstraintViolation<CancelamentoRequisicao>> violations = validator.validate(requisicao);

        assertEquals(1, violations.size());
        assertEquals("ID do paciente deve ter no máximo 50 caracteres", violations.iterator().next().getMessage());
    }

    @Test
    void quandoMotivoMuitoLongo_DeveRetornarViolacao() {
        String motivoMuitoLongo = "m".repeat(256); // 256 caracteres, excede o limite de 255

        CancelamentoRequisicao requisicao = new CancelamentoRequisicao(
                "agendamento123",
                "paciente456",
                motivoMuitoLongo
        );

        Set<ConstraintViolation<CancelamentoRequisicao>> violations = validator.validate(requisicao);

        assertEquals(1, violations.size());
        assertEquals("Motivo deve ter no máximo 255 caracteres", violations.iterator().next().getMessage());
    }

    @Test
    void quandoMotivoNulo_NaoDeveRetornarViolacao() {
        CancelamentoRequisicao requisicao = new CancelamentoRequisicao(
                "agendamento123",
                "paciente456",
                null
        );

        Set<ConstraintViolation<CancelamentoRequisicao>> violations = validator.validate(requisicao);

        assertTrue(violations.isEmpty());
    }

    @Test
    void quandoMotivoVazio_NaoDeveRetornarViolacao() {
        CancelamentoRequisicao requisicao = new CancelamentoRequisicao(
                "agendamento123",
                "paciente456",
                ""
        );

        Set<ConstraintViolation<CancelamentoRequisicao>> violations = validator.validate(requisicao);

        assertTrue(violations.isEmpty());
    }
}
