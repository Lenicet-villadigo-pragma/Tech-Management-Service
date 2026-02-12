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

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
}
