package reactivechallenge.pragma.techmanagementservice.usecase;

import reactivechallenge.pragma.techmanagementservice.api.IRetrieveTechnologyServicePort;
import reactivechallenge.pragma.techmanagementservice.model.TechnologyModel;
import reactivechallenge.pragma.techmanagementservice.spi.ITechnologyRepositoryPort;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class TechnologyRetrieverService implements IRetrieveTechnologyServicePort {

    private final ITechnologyRepositoryPort technologyRepositoryPort;

    public TechnologyRetrieverService(ITechnologyRepositoryPort technologyRepositoryPort){
        this.technologyRepositoryPort = technologyRepositoryPort;
    }

    @Override
    public Mono<Boolean> verifyIfExists(List<Long> ids) {
        return technologyRepositoryPort.exists(ids);
    }

    @Override
    public Flux<TechnologyModel> getTechnologiesByIds(List<Long> ids) {
        if(ids == null || ids.isEmpty()){
            return Flux.empty();
        }
        return technologyRepositoryPort.findAllById(ids.stream().filter(Objects::nonNull).toList());
    }
}
