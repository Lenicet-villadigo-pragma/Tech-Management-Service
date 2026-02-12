package reactivechallenge.pragma.techmanagementservice.usecase;

import reactivechallenge.pragma.techmanagementservice.api.IRetrieveTechnologyServicePort;
import reactivechallenge.pragma.techmanagementservice.model.TechnologyModel;
import reactivechallenge.pragma.techmanagementservice.spi.ITechnologyRepositoryPort;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
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
    public List<Long> verifyTechIds(String techIdsAsString){
        Optional<String> techIdsAsStringOpt = Optional.ofNullable(techIdsAsString);
        List<String> techIds = techIdsAsStringOpt
                .map(idList -> Arrays.stream(idList.split(","))
                        .map(idString -> idString.replaceAll("[\"/\\\\]", "").trim())
                        .toList())
                .orElse(Collections.emptyList());

        if(techIds.isEmpty()){
            throw new IllegalArgumentException("No se proporcionaron IDs de tecnologías. Asegúrate de incluir el " +
                    "parámetro 'techIds' con al menos un ID.");
        }
        if(techIds.stream().anyMatch(id -> !id.matches("\\d+"))){
            throw new IllegalArgumentException("Formato de IDs inválido. Todos los IDs deben ser números.");
        }

        return techIds.stream().map(Long::valueOf).toList();
    }

    @Override
    public Flux<TechnologyModel> getTechnologiesByIds(List<Long> ids) {
        if(ids == null || ids.isEmpty()){
            return Flux.empty();
        }
        return technologyRepositoryPort.findAllById(ids.stream().filter(Objects::nonNull).toList());
    }
}
