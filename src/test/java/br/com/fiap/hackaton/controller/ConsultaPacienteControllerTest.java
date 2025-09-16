package br.com.fiap.hackaton.controller;

import br.com.fiap.hackaton.config.TestSecurityConfig;
import br.com.fiap.hackaton.dto.ConsultaPacienteResposta;
import br.com.fiap.hackaton.service.ConsultaPacienteService;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ConsultaPacienteController.class)
@Import(TestSecurityConfig.class)
public class ConsultaPacienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ConsultaPacienteService consultaPacienteService;

    @Test
    @DisplayName("Deve retornar lista de consultas ativas de um paciente")
    public void testBuscarConsultasAtivas() throws Exception {
        String pacienteId = "p1";
        List<ConsultaPacienteResposta> consultas = Arrays.asList(
                criarConsultaResposta("a1"),
                criarConsultaResposta("a2")
        );

        when(consultaPacienteService.buscarConsultasAtivas(pacienteId)).thenReturn(consultas);

        mockMvc.perform(get("/api/pacientes/{pacienteId}/consultas/ativas", pacienteId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value("a1"))
                .andExpect(jsonPath("$[1].id").value("a2"));
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando paciente não tem consultas ativas")
    public void testBuscarConsultasAtivasVazia() throws Exception {
        String pacienteId = "p3";

        when(consultaPacienteService.buscarConsultasAtivas(pacienteId)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/pacientes/{pacienteId}/consultas/ativas", pacienteId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @DisplayName("Deve retornar histórico de consultas de um paciente")
    public void testBuscarHistoricoConsultas() throws Exception {
        String pacienteId = "p1";
        List<ConsultaPacienteResposta> consultas = Arrays.asList(
                criarConsultaResposta("a3"),
                criarConsultaResposta("a4")
        );

        when(consultaPacienteService.buscarHistoricoConsultas(pacienteId)).thenReturn(consultas);

        mockMvc.perform(get("/api/pacientes/{pacienteId}/consultas/historico", pacienteId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value("a3"))
                .andExpect(jsonPath("$[1].id").value("a4"));
    }

    @Test
    @DisplayName("Deve retornar todas as consultas de um paciente")
    public void testBuscarTodasConsultas() throws Exception {
        String pacienteId = "p1";
        List<ConsultaPacienteResposta> consultas = Arrays.asList(
                criarConsultaResposta("a1"),
                criarConsultaResposta("a2"),
                criarConsultaResposta("a3")
        );

        when(consultaPacienteService.buscarTodasConsultas(pacienteId)).thenReturn(consultas);

        mockMvc.perform(get("/api/pacientes/{pacienteId}/consultas/todas", pacienteId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].id").value("a1"))
                .andExpect(jsonPath("$[1].id").value("a2"))
                .andExpect(jsonPath("$[2].id").value("a3"));
    }

    @Test
    @DisplayName("Deve retornar erro 400 quando ID do paciente é inválido")
    public void testValidacaoIdPaciente() throws Exception {
        String pacienteIdInvalido = "paciente_id_muito_longo_que_ultrapassa_o_limite_de_cinquenta_caracteres_estabelecido";

        mockMvc.perform(get("/api/pacientes/{pacienteId}/consultas/ativas", pacienteIdInvalido)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$['buscarConsultasAtivas.pacienteId']").exists());
    }

    @Test
    @DisplayName("Deve retornar erro 400 quando ID do paciente está em branco")
    public void testValidacaoIdPacienteEmBranco() throws Exception {
        mockMvc.perform(get("/api/pacientes/{pacienteId}/consultas/ativas", " ")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$['buscarConsultasAtivas.pacienteId']").exists());
    }

    private ConsultaPacienteResposta criarConsultaResposta(String id) {
        ConsultaPacienteResposta resposta = new ConsultaPacienteResposta();
        resposta.setId(id);
        resposta.setDataHora(LocalDateTime.now().plusDays(1));
        resposta.setUnidadeNome("Unidade Teste");
        resposta.setUnidadeLocalizacao("Local Teste");
        resposta.setProfissionalId("prof1");
        resposta.setTipoAtendimento(br.com.fiap.hackaton.enums.TipoAgendamento.CLINICO_GERAL);
        resposta.setStatus(br.com.fiap.hackaton.enums.StatusAgendamento.AGENDADA);
        return resposta;
    }
}
