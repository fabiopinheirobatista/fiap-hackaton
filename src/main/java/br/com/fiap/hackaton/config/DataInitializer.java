package br.com.fiap.hackaton.config;

import br.com.fiap.hackaton.service.ConfiguracaoDadosService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements ApplicationRunner {

    private final ConfiguracaoDadosService configuracaoDadosService;

    public DataInitializer(ConfiguracaoDadosService configuracaoDadosService) {
        this.configuracaoDadosService = configuracaoDadosService;
    }

    @Override
    public void run(ApplicationArguments args) {
        configuracaoDadosService.reiniciarDados();
    }
}
