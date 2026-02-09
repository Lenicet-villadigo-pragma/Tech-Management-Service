package reactivechallenge.pragma.techmanagementservice.usecase;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactivechallenge.pragma.techmanagementservice.model.TechnologyModel;
import reactivechallenge.pragma.techmanagementservice.spi.ITechnologyRepositoryPort;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TechnologyCreatorUseCaseTest {

    @Mock
    private ITechnologyRepositoryPort technologyRepositoryPort;

    @InjectMocks
    private TechnologyCreatorUseCase technologyCreatorUseCase;

    @Test
    @DisplayName("Create technology successfully with normalized name")
    void createTechnologySuccess() {
        // Arrange
        TechnologyModel inputModel = new TechnologyModel(null, "  JAVA  Programming  ", "Description");
        TechnologyModel expectedSavedModel = new TechnologyModel(null, "java programming", "Description");
        TechnologyModel returnedModel = new TechnologyModel(1L, "java programming", "Description");

        when(technologyRepositoryPort.save(any(TechnologyModel.class))).thenReturn(Mono.just(returnedModel));

        // Act
        Mono<TechnologyModel> result = technologyCreatorUseCase.createTechnology(inputModel);

        // Assert
        StepVerifier.create(result)
                .expectNext(returnedModel)
                .verifyComplete();

        verify(technologyRepositoryPort).save(expectedSavedModel);
    }

    @Test
    @DisplayName("Create technology throws exception when name is null")
    void createTechnologyThrowsExceptionWhenNameIsNull() {
        // Arrange
        TechnologyModel inputModel = new TechnologyModel(null, null, "Description");

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                technologyCreatorUseCase.createTechnology(inputModel)
        );

        assertEquals("El nombre no puede ser nulo", exception.getMessage());

        verify(technologyRepositoryPort, times(0)).save(any());
    }

    @Test
    @DisplayName("Create technology throws exception when name is empty or blank")
    void createTechnologyThrowsExceptionWhenNameIsBlank() {
        // Arrange
        TechnologyModel inputModel = new TechnologyModel(null, "   ", "Description");

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                technologyCreatorUseCase.createTechnology(inputModel)
        );

        assertEquals("El nombre no puede estar vacío", exception.getMessage());

        verify(technologyRepositoryPort, times(0)).save(any());
    }
}
