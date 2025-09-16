package br.com.fiap.jackaton.config;

import br.com.fiap.jackaton.service.DataSetupService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements ApplicationRunner {

    private final DataSetupService dataSetupService;

    public DataInitializer(DataSetupService dataSetupService) {
        this.dataSetupService = dataSetupService;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        dataSetupService.resetData();
    }
}
