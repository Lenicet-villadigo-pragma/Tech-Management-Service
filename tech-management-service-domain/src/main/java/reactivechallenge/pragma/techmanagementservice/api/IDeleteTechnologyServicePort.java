package reactivechallenge.pragma.techmanagementservice.api;

import reactor.core.publisher.Mono;

import java.util.List;

public interface IDeleteTechnologyServicePort {
    Mono<Void> deleteAllTechsWithIds(List<Long> ids);
}
