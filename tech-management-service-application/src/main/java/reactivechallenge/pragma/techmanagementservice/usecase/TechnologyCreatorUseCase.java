package reactivechallenge.pragma.techmanagementservice.usecase;

import reactivechallenge.pragma.techmanagementservice.api.IRegisterTechnologyServicePort;
import reactivechallenge.pragma.techmanagementservice.model.TechnologyModel;
import reactivechallenge.pragma.techmanagementservice.spi.ITechnologyRepositoryPort;
import reactor.core.publisher.Mono;

public class TechnologyCreatorUseCase implements IRegisterTechnologyServicePort {

    private final ITechnologyRepositoryPort technologyRepositoryPort;

    public TechnologyCreatorUseCase(ITechnologyRepositoryPort technologyRepositoryPort) {
        this.technologyRepositoryPort = technologyRepositoryPort;
    }

    @Override
    public Mono<TechnologyModel> createTechnology(TechnologyModel technologyModel) {
        return technologyRepositoryPort.save(validateTechnology(technologyModel));
    }

    private TechnologyModel validateTechnology(TechnologyModel technologyModel) {
        return new TechnologyModel(technologyModel.id(),
               validateName(technologyModel.name()),technologyModel.description());
    }

    private String validateName(String name) {
        if(name == null) {
            throw new IllegalArgumentException("El nombre no puede ser nulo");
        }
        String nameValidated = name.toLowerCase().replaceAll("\\s+", " ").trim();
        if(nameValidated.isBlank()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }
        return nameValidated;
    }
}
