package reactivechallenge.pragma.techmanagementservice.out.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactivechallenge.pragma.techmanagementservice.out.entity.TechnologyEntity;

@Repository
public interface ITechnologyRepository extends R2dbcRepository<TechnologyEntity, Long> {

}
