package ch.admin.astra.vz.lc.api;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Singular;

import java.util.List;

@Schema(name = "AttributeGroup")
public record AttributeGroupDto(@NotNull String name, Long order, @Singular List<AttributeDto> attributes) {
}
