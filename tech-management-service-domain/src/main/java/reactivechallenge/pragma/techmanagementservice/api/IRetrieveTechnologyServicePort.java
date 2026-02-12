package reactivechallenge.pragma.techmanagementservice.api;

import reactor.core.publisher.Mono;

import java.util.List;

public interface IRetrieveTechnologyServicePort {
    Mono<Boolean> verifyIfExists(List<Long> ids);
    List<Long> verifyTechIds(String techIdsAsString);
}
