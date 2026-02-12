package reactivechallenge.pragma.techmanagementservice.out.repository.impl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import reactivechallenge.pragma.techmanagementservice.error.BusinessDomainException;
import reactivechallenge.pragma.techmanagementservice.exception.GenericDataBaseException;
import reactivechallenge.pragma.techmanagementservice.mapper.TechnologyEntityMapper;
import reactivechallenge.pragma.techmanagementservice.model.TechnologyModel;
import reactivechallenge.pragma.techmanagementservice.out.entity.TechnologyEntity;
import reactivechallenge.pragma.techmanagementservice.out.repository.ITechnologyRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import java.util.List;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
@ExtendWith(MockitoExtension.class)
class TechnologyRepositoryImplTest {

    @Mock
    private ITechnologyRepository technologyRepository;
    @Mock
    private TechnologyEntityMapper technologyEntityMapper;
    @InjectMocks
    private TechnologyRepositoryImpl technologyRepositoryImpl;


    @Test
    @DisplayName("Save technology successfully")
    void saveTechnologySuccess() {
        // Arrange
        TechnologyModel inputModel = new TechnologyModel(null, "Java", "Description");
        TechnologyEntity entityToSave = new TechnologyEntity(null, "Java", "Description");
        TechnologyEntity savedEntity = new TechnologyEntity(1L, "Java", "Description");
        TechnologyModel expectedModel = new TechnologyModel(1L, "Java", "Description");
        when(technologyEntityMapper.toEntity(inputModel)).thenReturn(entityToSave);
        when(technologyRepository.save(entityToSave)).thenReturn(Mono.just(savedEntity));
        when(technologyEntityMapper.toModel(savedEntity)).thenReturn(expectedModel);

        // Act
        Mono<TechnologyModel> result = technologyRepositoryImpl.save(inputModel);

        // Assert
        StepVerifier.create(result)
                .expectNext(expectedModel)
                .verifyComplete();
        verify(technologyRepository).save(entityToSave);
    }

    @Test
    @DisplayName("Save technology maps DataIntegrityViolationException to TechnologyAlreadyExistsException")
    void saveTechnologyMapsException() {
        // Arrange
        TechnologyModel inputModel = new TechnologyModel(null, "Java", "Description");
        TechnologyEntity entityToSave = new TechnologyEntity(null, "Java", "Description");
        DataIntegrityViolationException exception = new DataIntegrityViolationException("... technology.name_tecnology_unique ...");

        when(technologyEntityMapper.toEntity(inputModel)).thenReturn(entityToSave);
        when(technologyRepository.save(entityToSave)).thenReturn(Mono.error(exception));

        // Act
        Mono<TechnologyModel> result = technologyRepositoryImpl.save(inputModel);

        // Assert
        StepVerifier.create(result)
                .expectError(BusinessDomainException.class)
                .verify();
    }

    @Test
    @DisplayName("Exists returns true when technology exists")
    void existsReturnsTrueWhenTechnologyExists() {
        // Arrange
        List<Long> techId = List.of(1L);
        TechnologyEntity technologyEntity = new TechnologyEntity(1L, "Java", "Description");
        when(technologyRepository.findAllById(techId)).thenReturn(Flux.just(technologyEntity));

        // Act
        Mono<Boolean> result = technologyRepositoryImpl.exists(techId);

        // Assert
        StepVerifier.create(result)
                .expectNext(true)
                .verifyComplete();
        verify(technologyRepository).findAllById(techId);
    }

    @Test
    @DisplayName("Exists returns false when technology does not exist")
    void existsReturnsFalseWhenTechnologyDoesNotExist() {
        // Arrange
        List<Long> techId = List.of(999L);
        when(technologyRepository.findAllById(techId)).thenReturn(Flux.empty());

        // Act
        Mono<Boolean> result = technologyRepositoryImpl.exists(techId);

        // Assert
        StepVerifier.create(result)
                .expectNext(false)
                .verifyComplete();
        verify(technologyRepository).findAllById(techId);
    }

    @Test
    @DisplayName("Exists maps DataAccessException to GenericDataBaseException")
    void existsMapsException() {
        // Arrange
        List<Long> techId = List.of(1L);
        when(technologyRepository.findAllById(techId)).thenReturn(Flux.error(new RuntimeException("Database error")));

        // Act
        Mono<Boolean> result = technologyRepositoryImpl.exists(techId);

        // Assert
        StepVerifier.create(result)
                .expectError(GenericDataBaseException.class)
                .verify();
        verify(technologyRepository).findAllById(techId);
    }
}
