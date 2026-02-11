package reactivechallenge.pragma.techmanagementservice.spi;

import reactivechallenge.pragma.techmanagementservice.model.TechnologyModel;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ITechnologyRepositoryPort {

    Mono<TechnologyModel> save(TechnologyModel technologyModel);
    Mono<Boolean> exists(List<Long> ids);
}