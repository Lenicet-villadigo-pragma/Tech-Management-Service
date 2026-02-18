package reactivechallenge.pragma.techmanagementservice.usecase;

import reactivechallenge.pragma.techmanagementservice.api.IDeleteTechnologyServicePort;
import reactivechallenge.pragma.techmanagementservice.model.TechnologyModel;
import reactivechallenge.pragma.techmanagementservice.spi.ITechnologyRepositoryPort;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;

public class TechnologyEraserUseCase implements IDeleteTechnologyServicePort {
    private final ITechnologyRepositoryPort technologyRepositoryPort;

    public TechnologyEraserUseCase(ITechnologyRepositoryPort technologyRepositoryPort) {
        this.technologyRepositoryPort = technologyRepositoryPort;
    }

    @Override
    public Mono<Void> deleteAllTechsWithIds(List<Long> ids) {
        if(ids == null || ids.isEmpty()){
            return Mono.empty();
        }

        return technologyRepositoryPort.findAllById(ids.stream().filter(Objects::nonNull).toList())
                .map(TechnologyModel::id)
                .collectList()
                .flatMap(technologyRepositoryPort::delteByIds)
                .then();
    }
}
