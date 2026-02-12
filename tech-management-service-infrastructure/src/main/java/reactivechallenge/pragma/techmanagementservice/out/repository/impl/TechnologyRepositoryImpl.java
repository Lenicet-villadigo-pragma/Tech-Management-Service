package reactivechallenge.pragma.techmanagementservice.out.repository.impl;

import org.springframework.stereotype.Component;
import reactivechallenge.pragma.techmanagementservice.mapper.DatabaseErrorMapper;
import reactivechallenge.pragma.techmanagementservice.mapper.TechnologyEntityMapper;
import reactivechallenge.pragma.techmanagementservice.model.TechnologyModel;
import reactivechallenge.pragma.techmanagementservice.out.repository.ITechnologyRepository;
import reactivechallenge.pragma.techmanagementservice.spi.ITechnologyRepositoryPort;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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

    @Override
    public Mono<Boolean> exists(List<Long> ids) {
        return technologyRepository.findAllById(ids)
                .collectList()
                .map(listTechs -> listTechs.size() == ids.size())
                .onErrorMap(DatabaseErrorMapper::map);
    }

    @Override
    public Flux<TechnologyModel> findAllById(List<Long> ids) {
        return technologyRepository.findAllById(ids)
                .map(technologyEntityMapper::toModel)
                .filter(Objects::nonNull)
                .onErrorMap(DatabaseErrorMapper::map);
    }


}
