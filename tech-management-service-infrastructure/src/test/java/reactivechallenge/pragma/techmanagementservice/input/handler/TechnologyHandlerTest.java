package reactivechallenge.pragma.techmanagementservice.input.handler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactivechallenge.pragma.techmanagementservice.api.IRegisterTechnologyServicePort;
import reactivechallenge.pragma.techmanagementservice.api.IRetrieveTechnologyServicePort;
import reactivechallenge.pragma.techmanagementservice.error.BusinessDomainException;
import reactivechallenge.pragma.techmanagementservice.input.dto.CreateTechnologyRequestDto;
import reactivechallenge.pragma.techmanagementservice.model.TechnologyModel;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class TechnologyHandlerTest {

    @Mock
    private IRegisterTechnologyServicePort technologyCreatorServicePort;

    @Mock
    private IRetrieveTechnologyServicePort retrieveTechnologyServicePort;

    private TechnologyHandler technologyHandler;

    @BeforeEach
    void setUp() {
        technologyHandler = new TechnologyHandler(technologyCreatorServicePort, retrieveTechnologyServicePort);
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
        given(retrieveTechnologyServicePort.verifyIfExists(techId)).willReturn(Mono.just(true));

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
        given(retrieveTechnologyServicePort.verifyIfExists(techIds)).willReturn(Mono.just(false));

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

        // Act & Assert
        StepVerifier.create(
                Mono.defer(() -> technologyHandler.verifyIfTechnologyExist(request))
        )
                .expectError(IllegalArgumentException.class)
                .verify();
    }


}
