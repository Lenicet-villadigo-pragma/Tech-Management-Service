package reactivechallenge.pragma.techmanagementservice.usecase;

import reactivechallenge.pragma.techmanagementservice.api.IRetrieveTechnologyServicePort;
import reactivechallenge.pragma.techmanagementservice.spi.ITechnologyRepositoryPort;
import reactor.core.publisher.Mono;

import java.util.List;

public class TechnologyRetrieverService implements IRetrieveTechnologyServicePort {

    private final ITechnologyRepositoryPort technologyRepositoryPort;

    public TechnologyRetrieverService(ITechnologyRepositoryPort technologyRepositoryPort){
        this.technologyRepositoryPort = technologyRepositoryPort;
    }

    @Override
    public Mono<Boolean> verifyIfExists(List<Long> ids) {
        return technologyRepositoryPort.exists(ids);
    }
}
