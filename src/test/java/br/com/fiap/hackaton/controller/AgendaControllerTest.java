package br.com.fiap.hackaton.controller;

import br.com.fiap.hackaton.dto.AgendaRequest;
import br.com.fiap.hackaton.dto.AgendaResponse;
import br.com.fiap.hackaton.dto.BloqueioRequest;
import br.com.fiap.hackaton.enums.TipoAgendamento;
import br.com.fiap.hackaton.service.ServicoAgenda;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AgendaController.class)
@TestPropertySource(properties = {
    "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration"
})
@AutoConfigureWebMvc
public class AgendaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ServicoAgenda servicoAgenda;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void deveValidarCamposObrigatoriosAoVisualizarAgenda() throws Exception {
        AgendaRequest request = new AgendaRequest();
        request.setData(LocalDate.now());

        mockMvc.perform(get("/api/agenda/visualizar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void deveValidarCamposObrigatoriosAoCriarBloqueio() throws Exception {
        BloqueioRequest requestInvalido = new BloqueioRequest();

        mockMvc.perform(post("/api/agenda/bloquear")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestInvalido)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void deveVisualizarAgendaComDadosValidos() throws Exception {
        AgendaRequest request = new AgendaRequest();
        request.setUnidadeId("u1");
        request.setData(LocalDate.now());
        request.setEspecialidade(TipoAgendamento.CLINICO_GERAL);

        AgendaResponse mockResponse = new AgendaResponse("u1", "Unidade Teste",
                LocalDate.now(), TipoAgendamento.CLINICO_GERAL, Collections.emptyList(), 0);

        when(servicoAgenda.visualizarAgenda(any(AgendaRequest.class))).thenReturn(mockResponse);

        mockMvc.perform(get("/api/agenda/visualizar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    public void deveCriarBloqueioComDadosValidos() throws Exception {
        BloqueioRequest request = new BloqueioRequest();
        request.setUnidadeId("u1");
        request.setDataHora(LocalDateTime.now().plusDays(1));
        request.setProfissionalId("prof1");
        request.setMotivo("Profissional de férias");

        doNothing().when(servicoAgenda).criarBloqueio(any(BloqueioRequest.class));

        mockMvc.perform(post("/api/agenda/bloquear")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    public void deveRetornarBadRequestQuandoUnidadeIdForMuitoLongo() throws Exception {
        AgendaRequest request = new AgendaRequest();
        request.setUnidadeId("a".repeat(51)); // Excede o limite de 50 caracteres
        request.setData(LocalDate.now());
        request.setEspecialidade(TipoAgendamento.CLINICO_GERAL);

        mockMvc.perform(get("/api/agenda/visualizar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}
