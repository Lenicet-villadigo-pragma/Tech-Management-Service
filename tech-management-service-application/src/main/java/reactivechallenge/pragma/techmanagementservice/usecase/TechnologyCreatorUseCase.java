package reactivechallenge.pragma.techmanagementservice.usecase;

import reactivechallenge.pragma.techmanagementservice.api.IRegisterTechnologyServicePort;
import reactivechallenge.pragma.techmanagementservice.validator.IStringVerifier;
import reactivechallenge.pragma.techmanagementservice.model.TechnologyModel;
import reactivechallenge.pragma.techmanagementservice.spi.ITechnologyRepositoryPort;
import reactor.core.publisher.Mono;

public class TechnologyCreatorUseCase implements IRegisterTechnologyServicePort, IStringVerifier {

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
               verify(technologyModel.name()),technologyModel.description());
    }
}
