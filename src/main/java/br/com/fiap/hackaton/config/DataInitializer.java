package br.com.fiap.hackaton.config;

import br.com.fiap.hackaton.service.ConfiguracaoDadosService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(1000) // Executa após todas as outras inicializações, incluindo Flyway
public class DataInitializer implements ApplicationListener<ApplicationReadyEvent> {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    private final ConfiguracaoDadosService configuracaoDadosService;

    public DataInitializer(ConfiguracaoDadosService configuracaoDadosService) {
        this.configuracaoDadosService = configuracaoDadosService;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        try {
            logger.info("Inicializando dados de configuração padrão...");
            configuracaoDadosService.reiniciarDados();
            logger.info("Dados de configuração inicializados com sucesso!");
        } catch (Exception ex) {
            logger.error("Falha ao inicializar dados de configuração: {}", ex.getMessage(), ex);
        }
    }
}
