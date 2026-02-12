package reactivechallenge.pragma.techmanagementservice.api;

import reactivechallenge.pragma.techmanagementservice.model.TechnologyModel;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface IRetrieveTechnologyServicePort {
    Mono<Boolean> verifyIfExists(List<Long> ids);
    List<Long> verifyTechIds(String techIdsAsString);
    Flux<TechnologyModel> getTechnologiesByIds(List<Long> ids);
}
