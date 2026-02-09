package reactivechallenge.pragma.techmanagementservice.out.repository.impl;

import org.springframework.stereotype.Component;
import reactivechallenge.pragma.techmanagementservice.mapper.DatabaseErrorMapper;
import reactivechallenge.pragma.techmanagementservice.mapper.TechnologyEntityMapper;
import reactivechallenge.pragma.techmanagementservice.model.TechnologyModel;
import reactivechallenge.pragma.techmanagementservice.out.repository.ITechnologyRepository;
import reactivechallenge.pragma.techmanagementservice.spi.ITechnologyRepositoryPort;
import reactor.core.publisher.Mono;

@Component
public record TechnologyRepositoryImpl(
        ITechnologyRepository technologyRepository,
        TechnologyEntityMapper technologyEntityMapper
) implements ITechnologyRepositoryPort {

    @Override
    public Mono<TechnologyModel> save(TechnologyModel technologyModel) {
        return technologyRepository.save(technologyEntityMapper.toEntity(technologyModel))
                .map(technologyEntityMapper::toModel)
                .onErrorMap(DatabaseErrorMapper::map);
    }
}
