package reactivechallenge.pragma.techmanagementservice.usecase;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactivechallenge.pragma.techmanagementservice.model.TechnologyModel;
import reactivechallenge.pragma.techmanagementservice.spi.ITechnologyRepositoryPort;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TechnologyRetrieverServiceTest {

    @Mock
    private ITechnologyRepositoryPort technologyRepositoryPort;

    @InjectMocks
    private TechnologyRetrieverService technologyRetrieverService;

    @Test
    @DisplayName("Verify if exists returns true when technology exists")
    void verifyIfExistsReturnsTrue() {
        // Arrange
        List<Long> techId = List.of(1L);
        when(technologyRepositoryPort.exists(techId)).thenReturn(Mono.just(true));

        // Act
        Mono<Boolean> result = technologyRetrieverService.verifyIfExists(techId);

        // Assert
        StepVerifier.create(result)
                .expectNext(true)
                .verifyComplete();

        verify(technologyRepositoryPort).exists(techId);
    }

    @Test
    @DisplayName("Verify if exists returns false when technology does not exist")
    void verifyIfExistsReturnsFalse() {
        // Arrange
        List<Long> techId = List.of(999L);
        when(technologyRepositoryPort.exists(techId)).thenReturn(Mono.just(false));

        // Act
        Mono<Boolean> result = technologyRetrieverService.verifyIfExists(techId);

        // Assert
        StepVerifier.create(result)
                .expectNext(false)
                .verifyComplete();

        verify(technologyRepositoryPort).exists(techId);
    }

    @Test
    @DisplayName("Verify if exists propagates error from repository")
    void verifyIfExistsPropagatesError() {
        // Arrange
        List<Long> techId = List.of(1L);
        RuntimeException expectedException = new RuntimeException("Database error");
        when(technologyRepositoryPort.exists(techId)).thenReturn(Mono.error(expectedException));

        // Act
        Mono<Boolean> result = technologyRetrieverService.verifyIfExists(techId);

        // Assert
        StepVerifier.create(result)
                .expectError(RuntimeException.class)
                .verify();

        verify(technologyRepositoryPort).exists(techId);
    }

    @Test
    @DisplayName("Verify if exists handles null id gracefully")
    void verifyIfExistsHandlesNullId() {
        // Arrange
        when(technologyRepositoryPort.exists(null)).thenReturn(Mono.just(false));

        // Act
        Mono<Boolean> result = technologyRetrieverService.verifyIfExists(null);

        // Assert
        StepVerifier.create(result)
                .expectNext(false)
                .verifyComplete();

        verify(technologyRepositoryPort).exists(null);
    }

    @Test
    @DisplayName("verifyTechIds retorna lista de Longs a partir de string de ids separados por comas")
    void verifyTechIdsReturnsListOfLongs() {
        // Arrange
        String techIdsAsString = "1, 2, 3";

        // Act
        List<Long> result = technologyRetrieverService.verifyTechIds(techIdsAsString);

        // Assert
        assert result.equals(List.of(1L, 2L, 3L));
    }

    @Test
    @DisplayName("verifyTechIds lanza IllegalArgumentException cuando se pasa un string nulo")
    void verifyTechIdsThrowExeptionWhenNullString() {
        // Arrange
        RuntimeException exceptionExpected = new IllegalArgumentException("No se proporcionaron IDs de tecnologías. Asegúrate de incluir el " +
                "parámetro 'techIds' con al menos un ID.");
        RuntimeException exceptionObtained=null;

        // Act
        try {
            technologyRetrieverService.verifyTechIds(null);
        } catch (IllegalArgumentException e) {
            exceptionObtained = e;
        }

        // Assert
        assert exceptionObtained!=null && exceptionObtained.getMessage().equals(exceptionExpected.getMessage());
    }

    @Test
    @DisplayName("verifyTechIds lanza IllegalArgumentException cuando se pasa un string de letras separados por coma")
    void verifyTechIdsThrowExeptionWhenStringIsNotNumbers() {
        // Arrange
        String ids = "a, b, c";
        RuntimeException exceptionExpected = new IllegalArgumentException("Formato de IDs inválido. Todos los IDs deben ser números.");
        RuntimeException exceptionObtained=null;

        // Act
        try {
            technologyRetrieverService.verifyTechIds(ids);
        } catch (IllegalArgumentException e) {
            exceptionObtained = e;
        }

        // Assert
        assert exceptionObtained!=null && exceptionObtained.getMessage().equals(exceptionExpected.getMessage());
    }


    @Test
    @DisplayName("getAllTechnologiesById returns list of technologies when they exist")
    void getTechnologiesByIdsReturnsList() {
        // Arrange
        List<Long> techIds = List.of(1L);
        TechnologyModel technologyModel=new TechnologyModel(null, "Java", "Programming language");
        when(technologyRepositoryPort.findAllById(techIds)).thenReturn(Flux.just(technologyModel));

        // Act
        Flux<TechnologyModel> result = technologyRetrieverService.getTechnologiesByIds(techIds);

        // Assert
        StepVerifier.create(result)
                .expectNext(technologyModel)
                .verifyComplete();

        verify(technologyRepositoryPort).findAllById(techIds);
    }

    @Test
    @DisplayName("getAllTechnologiesById returns empty when list of ids is empty")
    void getTechnologiesByIdsReturnsEmpty() {
        // Arrange
        List<Long> techIds = new ArrayList<>();


        // Act
        Flux<TechnologyModel> result = technologyRetrieverService.getTechnologiesByIds(techIds);

        // Assert
        StepVerifier.create(result)
                .expectNextCount(0)
                .verifyComplete();

        verify(technologyRepositoryPort, never()).findAllById(techIds);
    }

    @Test
    @DisplayName("getAllTechnologiesById returns empty when list of ids is null")
    void getTechnologiesByIdsReturnsEmpty2() {
        // Act
        Flux<TechnologyModel> result = technologyRetrieverService.getTechnologiesByIds(null);

        // Assert
        StepVerifier.create(result)
                .expectNextCount(0)
                .verifyComplete();

        verify(technologyRepositoryPort, never()).findAllById(null);
    }
}
