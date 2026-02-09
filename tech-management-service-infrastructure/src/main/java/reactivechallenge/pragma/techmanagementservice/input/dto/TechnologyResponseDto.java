package reactivechallenge.pragma.techmanagementservice.input.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import reactivechallenge.pragma.techmanagementservice.model.TechnologyModel;

public record TechnologyResponseDto (@NotNull @NotBlank String name, @NotNull @NotBlank String description) {
    public TechnologyModel toModel() {
        return new TechnologyModel(null, this.name, this.description);
    }

    public static TechnologyResponseDto fromModel(TechnologyModel technologyModel) {
        return new TechnologyResponseDto(technologyModel.name(), technologyModel.description());
    }
}
