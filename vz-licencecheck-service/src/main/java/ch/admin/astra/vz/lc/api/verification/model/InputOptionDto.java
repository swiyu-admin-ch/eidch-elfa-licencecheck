package ch.admin.astra.vz.lc.api.verification.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(name = "InputOption")
public record InputOptionDto(@NotNull String key, @NotNull String value, String image) {
}
