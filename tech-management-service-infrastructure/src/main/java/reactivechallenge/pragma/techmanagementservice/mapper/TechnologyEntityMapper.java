package reactivechallenge.pragma.techmanagementservice.mapper;

import reactivechallenge.pragma.techmanagementservice.model.TechnologyModel;
import reactivechallenge.pragma.techmanagementservice.out.entity.TechnologyEntity;
import org.springframework.stereotype.Component;

@Component
public class TechnologyEntityMapper {

    public TechnologyModel toModel(TechnologyEntity entity) {
        if (entity == null) {
            return null;
        }
        return new TechnologyModel(
                entity.id(),
                entity.name(),
                entity.description()
        );
    }

    public TechnologyEntity toEntity(TechnologyModel model) {
        if (model == null) {
            return null;
        }
        return new TechnologyEntity(
                model.id(),
                model.name(),
                model.description()
        );
    }
}
