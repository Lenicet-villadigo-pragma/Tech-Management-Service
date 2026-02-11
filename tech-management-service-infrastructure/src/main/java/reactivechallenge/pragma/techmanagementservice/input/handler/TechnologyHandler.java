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
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
        List<String> techIds = request.queryParam("techIds")
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



}
