package reactivechallenge.pragma.techmanagementservice.api;

import reactivechallenge.pragma.techmanagementservice.model.TechnologyModel;
import reactor.core.publisher.Mono;

public interface IRegisterTechnologyServicePort {
    Mono<TechnologyModel> createTechnology(TechnologyModel technologyModel);
}
