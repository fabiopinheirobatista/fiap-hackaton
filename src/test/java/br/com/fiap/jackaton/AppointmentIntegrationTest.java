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

import java.util.Optional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AppointmentIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Test
    void suggestAndConfirmFlow_persistsAppointment() throws Exception {
        AppointmentRequest req = new AppointmentRequest();
        req.setPatientId("p1");
        req.setType(br.com.fiap.jackaton.enums.AppointmentType.CLINICO_GERAL);
        req.setSymptoms("dor de cabeça");
        req.setLocation("Centro");
        req.setUrgency(3);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<AppointmentRequest> entity = new HttpEntity<>(req, headers);
        ResponseEntity<SuggestionResponse> sugResp = restTemplate.postForEntity("/api/appointments/suggest", entity, SuggestionResponse.class);
        Assertions.assertEquals(200, sugResp.getStatusCodeValue());
        SuggestionResponse suggestion = sugResp.getBody();
        Assertions.assertNotNull(suggestion);
        Assertions.assertNotNull(suggestion.getUnitId());
        Assertions.assertNotNull(suggestion.getDateTime());

        ConfirmationRequest conf = new ConfirmationRequest();
        conf.setPatientId("p1");
        conf.setUnitId(suggestion.getUnitId());
        conf.setDateTime(suggestion.getDateTime());
        conf.setProfessionalId(suggestion.getProfessionalId());
        conf.setType(br.com.fiap.jackaton.enums.AppointmentType.CLINICO_GERAL);

        HttpEntity<ConfirmationRequest> confEntity = new HttpEntity<>(conf, headers);
        ResponseEntity<AppointmentEntity> confResp = restTemplate.postForEntity("/api/appointments/confirm", confEntity, AppointmentEntity.class);
        Assertions.assertEquals(201, confResp.getStatusCodeValue());
        AppointmentEntity created = confResp.getBody();
        Assertions.assertNotNull(created);
        Assertions.assertNotNull(created.getId());

        Optional<AppointmentEntity> fromDb = appointmentRepository.findById(created.getId());
        Assertions.assertTrue(fromDb.isPresent());
        AppointmentEntity persisted = fromDb.get();
        Assertions.assertEquals("p1", persisted.getPatientId());
        Assertions.assertEquals(suggestion.getUnitId(), persisted.getUnitId());
        Assertions.assertEquals(suggestion.getDateTime().withNano(0), persisted.getDateTime().withNano(0));
    }
}

