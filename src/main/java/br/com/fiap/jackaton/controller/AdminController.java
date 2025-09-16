package br.com.fiap.jackaton.controller;

import br.com.fiap.jackaton.service.DataSetupService;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final JdbcTemplate jdbcTemplate;
    private final DataSetupService dataSetupService;

    public AdminController(JdbcTemplate jdbcTemplate, DataSetupService dataSetupService) {
        this.jdbcTemplate = jdbcTemplate;
        this.dataSetupService = dataSetupService;
    }

    @GetMapping("/flyway/history")
    public List<Map<String, Object>> flywayHistory() {
        try {
            return jdbcTemplate.queryForList("SELECT installed_rank, version, description, type, installed_by, installed_on FROM flyway_schema_history ORDER BY installed_rank");
        } catch (Exception e) {
            return List.of(Map.of("error", "could not read flyway_schema_history: " + e.getMessage()));
        }
    }

    @PostMapping("/reset")
    public Map<String, Object> resetData() {
        try {
            dataSetupService.resetData();
            return Map.of("status", "ok");
        } catch (Exception e) {
            return Map.of("error", e.getMessage());
        }
    }
}
