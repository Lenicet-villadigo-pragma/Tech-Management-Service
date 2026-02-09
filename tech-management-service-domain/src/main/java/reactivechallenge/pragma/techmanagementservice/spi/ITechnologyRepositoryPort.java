package reactivechallenge.pragma.techmanagementservice.spi;

import reactivechallenge.pragma.techmanagementservice.model.TechnologyModel;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ITechnologyRepositoryPort {

    Mono<TechnologyModel> save(TechnologyModel technologyModel);
}