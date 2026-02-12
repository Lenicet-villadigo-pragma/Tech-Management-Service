package reactivechallenge.pragma.techmanagementservice.input.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import reactivechallenge.pragma.techmanagementservice.model.TechnologyModel;

public record ListTechnologiesResponseDto(@NotNull Long id,  @NotNull @NotBlank String name) {

    public static ListTechnologiesResponseDto fromModel(TechnologyModel technologyModel) {
        return new ListTechnologiesResponseDto(technologyModel.id(), technologyModel.name());
    }
}
