package reactivechallenge.pragma.techmanagementservice.input.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import reactivechallenge.pragma.techmanagementservice.model.TechnologyModel;

public record TechnologyRequestDto (
        @NotNull @NotBlank @Size(max = 50, message = "El nombre debe tener máximo 50 caracteres", min = 1) String name,
        @NotNull @NotBlank @Size(max = 90, message = "La descripción debe tener máximo 90 caracteres", min = 1) String description
) {

    public TechnologyModel toModel() {
        return new TechnologyModel(null, this.name, this.description);
    }

    public static TechnologyRequestDto fromModel(TechnologyModel technologyModel) {
        return new TechnologyRequestDto(technologyModel.name(), technologyModel.description());
    }
}
