package reactivechallenge.pragma.techmanagementservice.input.handler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactivechallenge.pragma.techmanagementservice.api.IRegisterTechnologyServicePort;
import reactivechallenge.pragma.techmanagementservice.error.BusinessDomainException;
import reactivechallenge.pragma.techmanagementservice.input.dto.TechnologyRequestDto;
import reactivechallenge.pragma.techmanagementservice.model.TechnologyModel;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class TechnologyHandlerTest {

    @Mock
    private IRegisterTechnologyServicePort technologyCreatorServicePort;

    // We use a real validator to test the annotations
    private Validator validator;

    private TechnologyHandler technologyHandler;

    @BeforeEach
    void setUp() {
        LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
        localValidatorFactoryBean.afterPropertiesSet();
        this.validator = localValidatorFactoryBean;

        technologyHandler = new TechnologyHandler(technologyCreatorServicePort, validator);
    }

    @Test
    @DisplayName("Create technology fails validation when name is blank")
    void createTechnologyValidationFailureForInvalidName() {
        // Arrange
        TechnologyRequestDto invalidDto = new TechnologyRequestDto("", "Valid description");
        ServerRequest request = mock(ServerRequest.class);

        given(request.bodyToMono(TechnologyRequestDto.class)).willReturn(Mono.just(invalidDto));

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
        TechnologyRequestDto invalidDto = new TechnologyRequestDto("valid name", "");
        ServerRequest request = mock(ServerRequest.class);

        given(request.bodyToMono(TechnologyRequestDto.class)).willReturn(Mono.just(invalidDto));

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
        TechnologyRequestDto validDto = new TechnologyRequestDto("Java", "Valid description");
        ServerRequest request = mock(ServerRequest.class);

        given(request.bodyToMono(TechnologyRequestDto.class)).willReturn(Mono.just(validDto));
        given(technologyCreatorServicePort.createTechnology(any(TechnologyModel.class)))
                .willReturn(Mono.just(new TechnologyModel(1L, "Java", "Valid description")));

        // Act
        Mono<ServerResponse> responseMono = technologyHandler.createTechnology(request);

        // Assert
        StepVerifier.create(responseMono)
                .expectNextMatches(serverResponse -> serverResponse.statusCode() == HttpStatus.CREATED)
                .verifyComplete();
    }
}
