package ch.admin.astra.vz.lc.api;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(name = "Attribute")
public record AttributeDto(@NotNull String name, @NotNull String type, Long order) {
}