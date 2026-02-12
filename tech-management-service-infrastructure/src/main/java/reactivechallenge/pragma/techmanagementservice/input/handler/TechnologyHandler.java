package reactivechallenge.pragma.techmanagementservice.input.handler;

import lombok.RequiredArgsConstructor;
import reactivechallenge.pragma.techmanagementservice.api.IRegisterTechnologyServicePort;
import reactivechallenge.pragma.techmanagementservice.api.IRetrieveTechnologyServicePort;
import reactivechallenge.pragma.techmanagementservice.input.dto.CreateTechnologyRequestDto;
import reactivechallenge.pragma.techmanagementservice.input.dto.CreateTechnologyResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactivechallenge.pragma.techmanagementservice.input.dto.ListTechnologiesResponseDto;
import reactivechallenge.pragma.techmanagementservice.model.TechnologyModel;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class TechnologyHandler {

    private final IRegisterTechnologyServicePort technologyCreatorServicePort;
    private final IRetrieveTechnologyServicePort retrieveTechnologyServicePort;

    public Mono<ServerResponse> createTechnology(ServerRequest request) {
        return request.bodyToMono(CreateTechnologyRequestDto.class)
                .map(CreateTechnologyRequestDto::toModel)
                .flatMap(technologyCreatorServicePort::createTechnology)
                .map(CreateTechnologyResponseDto::fromModel)
                .flatMap(dto -> ServerResponse.status(HttpStatus.CREATED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(dto));
    }

    public Mono<ServerResponse> verifyIfTechnologyExist(ServerRequest request) {
        return Mono.just(getTechIdsFromRequest(request))
        .flatMap(retrieveTechnologyServicePort::verifyIfExists)
        .flatMap(exists -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(exists))
        .onErrorResume(NumberFormatException.class,
                e -> ServerResponse.badRequest()
                        .bodyValue("Formato de IDs inválido. Deben ser números."));
    }

    private List<Long> getTechIdsFromRequest(ServerRequest request) {
       Optional<String> stringTechIds =  request.queryParam("techIds");
       return retrieveTechnologyServicePort.verifyTechIds(stringTechIds.orElse(null));
    }

    public Mono<ServerResponse> getTechnologiesById(ServerRequest request) {
        return Flux.just(getTechIdsFromRequest(request))
                .flatMap(retrieveTechnologyServicePort::getTechnologiesByIds)
                .map(ListTechnologiesResponseDto::fromModel)
                .collectList()
                .flatMap(dto -> ServerResponse.status(HttpStatus.CREATED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(dto));
    }
}
