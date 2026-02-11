package reactivechallenge.pragma.techmanagementservice.input.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import reactivechallenge.pragma.techmanagementservice.model.TechnologyModel;

public record CreateTechnologyResponseDto(@NotNull @NotBlank String name, @NotNull @NotBlank String description) {

    public static CreateTechnologyResponseDto fromModel(TechnologyModel technologyModel) {
        return new CreateTechnologyResponseDto(technologyModel.name(), technologyModel.description());
    }
}
