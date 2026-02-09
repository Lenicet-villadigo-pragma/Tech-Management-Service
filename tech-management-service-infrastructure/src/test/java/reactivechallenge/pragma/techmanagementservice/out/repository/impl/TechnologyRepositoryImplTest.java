package reactivechallenge.pragma.techmanagementservice.out.repository.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import reactivechallenge.pragma.techmanagementservice.error.RegisterAlreadyExistsException;
import reactivechallenge.pragma.techmanagementservice.mapper.TechnologyEntityMapper;
import reactivechallenge.pragma.techmanagementservice.model.TechnologyModel;
import reactivechallenge.pragma.techmanagementservice.out.entity.TechnologyEntity;
import reactivechallenge.pragma.techmanagementservice.out.repository.ITechnologyRepository;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
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
                .expectError(RegisterAlreadyExistsException.class)
                .verify();
    }
}
