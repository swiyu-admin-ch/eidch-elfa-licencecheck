package ch.admin.astra.vz.lc.api.verification.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Singular;

import java.util.List;
import java.util.UUID;

@Schema(name = "UseCase")
public record UseCaseDto(@NotNull UUID id, @NotNull String title, String description, Long order,
                         @Singular List<AttributeGroupDto> attributeGroups) {
}
