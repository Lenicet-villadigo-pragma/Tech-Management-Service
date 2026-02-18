package reactivechallenge.pragma.techmanagementservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactivechallenge.pragma.techmanagementservice.api.IDeleteTechnologyServicePort;
import reactivechallenge.pragma.techmanagementservice.api.IRegisterTechnologyServicePort;
import reactivechallenge.pragma.techmanagementservice.api.IRetrieveTechnologyServicePort;
import reactivechallenge.pragma.techmanagementservice.spi.ITechnologyRepositoryPort;
import reactivechallenge.pragma.techmanagementservice.usecase.TechnologyCreatorUseCase;
import reactivechallenge.pragma.techmanagementservice.usecase.TechnologyEraserUseCase;
import reactivechallenge.pragma.techmanagementservice.usecase.TechnologyRetrieverService;

@Configuration
public class UseCaseConfig {

    @Bean
    public IRegisterTechnologyServicePort technologyCreatorServicePort(ITechnologyRepositoryPort technologyRepositoryPort) {
        return new TechnologyCreatorUseCase(technologyRepositoryPort);
    }

    @Bean
    public IRetrieveTechnologyServicePort technologyRetrieverServicePort(ITechnologyRepositoryPort technologyRepositoryPort) {
        return new TechnologyRetrieverService(technologyRepositoryPort);
    }

    @Bean
    public IDeleteTechnologyServicePort technologyEraserServicePort(ITechnologyRepositoryPort technologyRepositoryPort) {
        return new TechnologyEraserUseCase(technologyRepositoryPort);
    }

}
