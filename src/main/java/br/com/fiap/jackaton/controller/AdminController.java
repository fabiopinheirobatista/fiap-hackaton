package br.com.fiap.jackaton.controller;

import br.com.fiap.jackaton.service.ConfiguracaoDadosService;
import org.springframework.http.ResponseEntity;
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
    private final ConfiguracaoDadosService configuracaoDadosService;

    public AdminController(JdbcTemplate jdbcTemplate, ConfiguracaoDadosService configuracaoDadosService) {
        this.jdbcTemplate = jdbcTemplate;
        this.configuracaoDadosService = configuracaoDadosService;
    }

    @GetMapping("/flyway/history")
    public List<Map<String, Object>> flywayHistory() {
        try {
            return jdbcTemplate.queryForList("SELECT installed_rank, version, description, type, installed_by, installed_on FROM flyway_schema_history ORDER BY installed_rank");
        } catch (Exception e) {
            return List.of(Map.of("error", "Não foi possível ler flyway_schema_history: " + e.getMessage()));
        }
    }

    @PostMapping("/reset")
    public ResponseEntity<Void> reiniciarDados() {
        configuracaoDadosService.reiniciarDados();
        return ResponseEntity.ok().build();
    }
}
