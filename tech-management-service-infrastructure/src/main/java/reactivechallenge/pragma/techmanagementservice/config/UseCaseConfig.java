package reactivechallenge.pragma.techmanagementservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactivechallenge.pragma.techmanagementservice.api.IRegisterTechnologyServicePort;
import reactivechallenge.pragma.techmanagementservice.spi.ITechnologyRepositoryPort;
import reactivechallenge.pragma.techmanagementservice.usecase.TechnologyCreatorUseCase;

@Configuration
public class UseCaseConfig {

    @Bean
    public IRegisterTechnologyServicePort technologyCreatorServicePort(ITechnologyRepositoryPort technologyRepositoryPort) {
        return new TechnologyCreatorUseCase(technologyRepositoryPort);
    }

}
