package reactivechallenge.pragma.techmanagementservice.input.handler;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.EntityResponse;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactivechallenge.pragma.techmanagementservice.api.IDeleteTechnologyServicePort;
import reactivechallenge.pragma.techmanagementservice.api.IRegisterTechnologyServicePort;
import reactivechallenge.pragma.techmanagementservice.api.IRetrieveTechnologyServicePort;
import reactivechallenge.pragma.techmanagementservice.error.BusinessDomainException;
import reactivechallenge.pragma.techmanagementservice.input.dto.CreateTechnologyRequestDto;
import reactivechallenge.pragma.techmanagementservice.model.TechnologyModel;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class TechnologyHandlerTest {

    @Mock
    private IRegisterTechnologyServicePort technologyCreatorServicePort;

    @Mock
    private IRetrieveTechnologyServicePort retrieveTechnologyServicePort;

    @Mock
    private IDeleteTechnologyServicePort deleteTechnologyServicePort;

    private TechnologyHandler technologyHandler;

    @BeforeEach
    void setUp() {
        technologyHandler = new TechnologyHandler(technologyCreatorServicePort, retrieveTechnologyServicePort
        , deleteTechnologyServicePort);
    }

    @Test
    @DisplayName("Create technology fails validation when name is blank")
    void createTechnologyValidationFailureForInvalidName() {
        // Arrange
        CreateTechnologyRequestDto invalidDto = new CreateTechnologyRequestDto("", "Valid description");
        ServerRequest request = mock(ServerRequest.class);

        given(request.bodyToMono(CreateTechnologyRequestDto.class)).willReturn(Mono.just(invalidDto));

        // Act
        Mono<ServerResponse> responseMono = technologyHandler.createTechnology(request);

        // Assert
        StepVerifier.create(responseMono)
                .expectErrorMatches(BusinessDomainException.class::isInstance)
                .verify();
    }

    @Test
    @DisplayName("Create technology fails validation when description is blank")
    void createTechnologyValidationFailureForInvalidDescription() {
        // Arrange
        CreateTechnologyRequestDto invalidDto = new CreateTechnologyRequestDto("valid name", "");
        ServerRequest request = mock(ServerRequest.class);

        given(request.bodyToMono(CreateTechnologyRequestDto.class)).willReturn(Mono.just(invalidDto));

        // Act
        Mono<ServerResponse> responseMono = technologyHandler.createTechnology(request);

        // Assert
        StepVerifier.create(responseMono)
                .expectErrorMatches(BusinessDomainException.class::isInstance)
                .verify();
    }

    @Test
    @DisplayName("Create technology succeeds when dto is valid")
    void createTechnologySuccess() {
        // Arrange
        CreateTechnologyRequestDto validDto = new CreateTechnologyRequestDto("Java", "Valid description");
        ServerRequest request = mock(ServerRequest.class);

        given(request.bodyToMono(CreateTechnologyRequestDto.class)).willReturn(Mono.just(validDto));
        given(technologyCreatorServicePort.createTechnology(any(TechnologyModel.class)))
                .willReturn(Mono.just(new TechnologyModel(1L, "Java", "Valid description")));

        // Act
        Mono<ServerResponse> responseMono = technologyHandler.createTechnology(request);

        // Assert
        StepVerifier.create(responseMono)
                .expectNextMatches(serverResponse -> serverResponse.statusCode() == HttpStatus.CREATED)
                .verifyComplete();
    }

    @Test
    @DisplayName("Verify if technology exists returns true when technology exists")
    void verifyIfTechnologyExistReturnsTrue() {
        // Arrange
        List<Long> techId = List.of(1L);
        String id1 = "1";
        ServerRequest request = mock(ServerRequest.class);

        given(request.queryParam("techIds")).willReturn(Optional.of(id1));
        given(retrieveTechnologyServicePort.verifyIfExists(anyList())).willReturn(Mono.just(true));
        given(retrieveTechnologyServicePort.verifyTechIds(anyString())).willReturn(techId);

        // Act
        Mono<ServerResponse> responseMono = technologyHandler.verifyIfTechnologyExist(request);

        // Assert
        StepVerifier.create(responseMono)
                .expectNextMatches(serverResponse -> serverResponse.statusCode() == HttpStatus.OK)
                .verifyComplete();
    }

    @Test
    @DisplayName("Verify if technology exists returns false when technology does not exist")
    void verifyIfTechnologyExistReturnsFalse() {
        // Arrange
        String techId = "999";
        List<Long> techIds= List.of(999L);
        ServerRequest request = mock(ServerRequest.class);

        given(request.queryParam("techIds")).willReturn(Optional.of(techId));
        given(retrieveTechnologyServicePort.verifyIfExists(anyList())).willReturn(Mono.just(false));
        given(retrieveTechnologyServicePort.verifyTechIds(anyString())).willReturn(techIds);

        // Act
        Mono<ServerResponse> responseMono = technologyHandler.verifyIfTechnologyExist(request);

        // Assert
        StepVerifier.create(responseMono)
                .expectNextMatches(serverResponse -> serverResponse.statusCode() == HttpStatus.OK)
                .verifyComplete();
    }

    @Test
    @DisplayName("Verify if technology exists fails when techIds is not sent")
    void verifyIfTechnologyExistFailsWithInvalidTechId() {
        // Arrange
        ServerRequest request = mock(ServerRequest.class);

        given(request.queryParam("techIds")).willReturn(Optional.empty());
        given(retrieveTechnologyServicePort.verifyTechIds(null)).willThrow(new IllegalArgumentException());

        // Act & Assert
        StepVerifier.create(
                Mono.defer(() -> technologyHandler.verifyIfTechnologyExist(request))
        )
                .expectError(IllegalArgumentException.class)
                .verify();
    }

    @Test
    @DisplayName("Se obtiene error al intentar obtener tecnologías por ids cuando no se envían techIds")
    void getTechnologiesByIdFailsWithInvalidTechIds() {
        // Arrange
        ServerRequest request = mock(ServerRequest.class);
        IllegalArgumentException error =new IllegalArgumentException("No se proporcionaron IDs de tecnologías. Asegúrate de incluir el " +
                "parámetro 'techIds' con al menos un ID.");

        given(request.queryParam("techIds")).willReturn(Optional.empty());
        given(retrieveTechnologyServicePort.verifyTechIds(null)).willThrow(error);

        // Act & Assert
        StepVerifier.create(
                        Mono.defer(() -> technologyHandler.getTechnologiesById(request))
                )
                .expectError(IllegalArgumentException.class)
                .verify();
    }

    @Test
    @DisplayName("get technology by id returns empty list when ids do not exist")
    void getTechnologiesByIdReturnsEmpty() {
        // Arrange
        String techId = "999";
        List<Long> techIds= List.of(999L);
        ServerRequest request = mock(ServerRequest.class);

        given(request.queryParam("techIds")).willReturn(Optional.of(techId));
        given(retrieveTechnologyServicePort.verifyTechIds(techId)).willReturn(techIds);
        given(retrieveTechnologyServicePort.getTechnologiesByIds(techIds)).willReturn(Flux.empty());

        // Act
        Mono<ServerResponse> responseMono = technologyHandler.getTechnologiesById(request);

        // Assert
        StepVerifier.create(responseMono)
                .assertNext(serverResponse -> {
                    Assertions.assertEquals(HttpStatus.OK, serverResponse.statusCode());

                    if (serverResponse instanceof EntityResponse<?> entityResponse) {
                        List<?> body = (List<?>) entityResponse.entity();
                        Assertions.assertTrue(body.isEmpty());
                    }
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("get technology by id returns list when ids does exist")
    void getTechnologiesByIdReturnsData() {
        // Arrange
        String techId = "999";
        List<Long> techIds= List.of(999L);
        ServerRequest request = mock(ServerRequest.class);
        TechnologyModel technologyModel = new TechnologyModel(999L, "TechName", "TechDescription");

        given(request.queryParam("techIds")).willReturn(Optional.of(techId));
        given(retrieveTechnologyServicePort.verifyTechIds(techId)).willReturn(techIds);
        given(retrieveTechnologyServicePort.getTechnologiesByIds(techIds)).willReturn(Flux.just(technologyModel));

        // Act
        Mono<ServerResponse> responseMono = technologyHandler.getTechnologiesById(request);

        // Assert
        StepVerifier.create(responseMono)
                .assertNext(serverResponse -> {
                    Assertions.assertEquals(HttpStatus.OK, serverResponse.statusCode());

                    if (serverResponse instanceof EntityResponse<?> entityResponse) {
                        List<?> body = (List<?>) entityResponse.entity();
                        Assertions.assertEquals(1, body.size());
                    }
                })
                .verifyComplete();
    }


}
