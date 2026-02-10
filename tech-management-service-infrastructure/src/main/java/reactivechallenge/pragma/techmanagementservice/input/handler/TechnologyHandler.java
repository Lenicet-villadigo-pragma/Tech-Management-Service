package reactivechallenge.pragma.techmanagementservice.input.handler;

import lombok.RequiredArgsConstructor;
import reactivechallenge.pragma.techmanagementservice.api.IRegisterTechnologyServicePort;
import reactivechallenge.pragma.techmanagementservice.input.dto.TechnologyRequestDto;
import reactivechallenge.pragma.techmanagementservice.input.dto.TechnologyResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class TechnologyHandler {

    private final IRegisterTechnologyServicePort technologyCreatorServicePort;
    private final Validator validator;

    public Mono<ServerResponse> createTechnology(ServerRequest request) {
        return request.bodyToMono(TechnologyRequestDto.class)
                .map(TechnologyRequestDto::toModel)
                .flatMap(technologyCreatorServicePort::createTechnology)
                .map(TechnologyResponseDto::fromModel)
                .flatMap(dto -> ServerResponse.status(HttpStatus.CREATED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(dto));
    }
}
