package br.com.fiap.jackaton;

import br.com.fiap.jackaton.dto.AppointmentRequest;
import br.com.fiap.jackaton.dto.ConfirmationRequest;
import br.com.fiap.jackaton.dto.SuggestionResponse;
import br.com.fiap.jackaton.entity.AppointmentEntity;
import br.com.fiap.jackaton.repository.AppointmentRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AppointmentValidationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Test
    void suggest_missingPatientId_returns400() {
        AppointmentRequest req = new AppointmentRequest();
        req.setType(br.com.fiap.jackaton.enums.AppointmentType.CLINICO_GERAL);
        req.setLocation("Centro");
        req.setUrgency(2);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<AppointmentRequest> entity = new HttpEntity<>(req, headers);

        ResponseEntity<String[]> resp = restTemplate.postForEntity("/api/appointments/suggest", entity, String[].class);
        Assertions.assertEquals(400, resp.getStatusCodeValue());
    }

    @Test
    void suggest_inactivePatient_returns400() {
        AppointmentRequest req = new AppointmentRequest();
        req.setPatientId("p3");
        req.setType(br.com.fiap.jackaton.enums.AppointmentType.CLINICO_GERAL);
        req.setLocation("Centro");
        req.setUrgency(2);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<AppointmentRequest> entity = new HttpEntity<>(req, headers);

        ResponseEntity<String> resp = restTemplate.postForEntity("/api/appointments/suggest", entity, String.class);
        Assertions.assertEquals(400, resp.getStatusCodeValue());
        Assertions.assertTrue(resp.getBody().contains("Paciente inativo"));
    }

    @Test
    void suggest_typeNotAvailable_returns409() {
        AppointmentRequest req = new AppointmentRequest();
        req.setPatientId("p1");
        req.setType(br.com.fiap.jackaton.enums.AppointmentType.CARDIOLOGIA);
        req.setLocation("Bairro");
        req.setUrgency(2);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<AppointmentRequest> entity = new HttpEntity<>(req, headers);

        ResponseEntity<String> resp = restTemplate.postForEntity("/api/appointments/suggest", entity, String.class);
        Assertions.assertEquals(409, resp.getStatusCodeValue());
    }

    @Test
    void confirm_conflict_returns409() {
        AppointmentRequest req = new AppointmentRequest();
        req.setPatientId("p1");
        req.setType(br.com.fiap.jackaton.enums.AppointmentType.CLINICO_GERAL);
        req.setLocation("Centro");
        req.setUrgency(1);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<AppointmentRequest> entity = new HttpEntity<>(req, headers);
        ResponseEntity<SuggestionResponse> sugResp = restTemplate.postForEntity("/api/appointments/suggest", entity, SuggestionResponse.class);
        Assertions.assertEquals(200, sugResp.getStatusCodeValue());
        SuggestionResponse suggestion = sugResp.getBody();
        Assertions.assertNotNull(suggestion);

        ConfirmationRequest conf = new ConfirmationRequest();
        conf.setPatientId("p1");
        conf.setUnitId(suggestion.getUnitId());
        conf.setDateTime(suggestion.getDateTime());
        conf.setProfessionalId(suggestion.getProfessionalId());
        conf.setType(br.com.fiap.jackaton.enums.AppointmentType.CLINICO_GERAL);

        HttpEntity<ConfirmationRequest> confEntity = new HttpEntity<>(conf, headers);
        ResponseEntity<AppointmentEntity> first = restTemplate.postForEntity("/api/appointments/confirm", confEntity, AppointmentEntity.class);
        Assertions.assertEquals(201, first.getStatusCodeValue());

        ResponseEntity<String> second = restTemplate.postForEntity("/api/appointments/confirm", confEntity, String.class);
        Assertions.assertEquals(409, second.getStatusCodeValue());
    }
}

