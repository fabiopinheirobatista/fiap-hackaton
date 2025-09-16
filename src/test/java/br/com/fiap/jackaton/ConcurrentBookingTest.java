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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ConcurrentBookingTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Test
    void concurrentConfirm_attempts_onlyOneSucceeds() throws Exception {
        // Authenticate as admin for reset endpoint
        restTemplate.withBasicAuth("admin", "admin123").postForEntity("/admin/reset", null, Object.class);

        AppointmentRequest req = new AppointmentRequest();
        req.setPatientId("p2");
        req.setType(br.com.fiap.jackaton.enums.AppointmentType.CLINICO_GERAL);
        req.setSymptoms("febre");
        req.setLocation("Centro");
        req.setUrgency(2);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<AppointmentRequest> entity = new HttpEntity<>(req, headers);
        ResponseEntity<SuggestionResponse> sugResp = restTemplate.postForEntity("/api/appointments/suggest", entity, SuggestionResponse.class);
        Assertions.assertEquals(200, sugResp.getStatusCodeValue());
        SuggestionResponse suggestion = sugResp.getBody();
        Assertions.assertNotNull(suggestion);

        ConfirmationRequest conf = new ConfirmationRequest();
        conf.setPatientId("p2");
        conf.setUnitId(suggestion.getUnitId());
        conf.setDateTime(suggestion.getDateTime());
        conf.setProfessionalId(suggestion.getProfessionalId());
        conf.setType(br.com.fiap.jackaton.enums.AppointmentType.CLINICO_GERAL);

        int threads = 8;
        ExecutorService executor = Executors.newFixedThreadPool(threads);
        CountDownLatch ready = new CountDownLatch(threads);
        CountDownLatch start = new CountDownLatch(1);
        List<Future<ResponseEntity<AppointmentEntity>>> futures = new ArrayList<>();

        for (int i = 0; i < threads; i++) {
            futures.add(executor.submit(() -> {
                ready.countDown();
                start.await();
                HttpEntity<ConfirmationRequest> confEntity = new HttpEntity<>(conf, headers);
                try {
                    return restTemplate.postForEntity("/api/appointments/confirm", confEntity, AppointmentEntity.class);
                } catch (Exception e) {
                    return null;
                }
            }));
        }

        ready.await();
        start.countDown();

        int success = 0;
        int conflict = 0;
        for (Future<ResponseEntity<AppointmentEntity>> f : futures) {
            ResponseEntity<AppointmentEntity> r = f.get(5, TimeUnit.SECONDS);
            if (r == null) {
                conflict++;
                continue;
            }
            int status = r.getStatusCodeValue();
            if (status == 201) success++;
            else if (status == 409) conflict++;
            else if (status >= 400) conflict++;
        }

        executor.shutdownNow();

        Assertions.assertEquals(1, success);
        Assertions.assertTrue(conflict >= 1);

        long count = appointmentRepository.findAll().stream().filter(a -> a.getPatientId().equals("p2") && a.getUnitId().equals(suggestion.getUnitId()) && a.getDateTime().withNano(0).equals(suggestion.getDateTime().withNano(0))).count();
        Assertions.assertEquals(1, count);
    }
}
