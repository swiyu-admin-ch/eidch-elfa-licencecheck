package ch.admin.astra.vz.lc.api.verification.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Singular;

import java.util.List;

@Schema(name = "InputElement")
public record InputElementDto(@NotNull String type, @NotNull String name,
                              @Singular List<InputOptionDto> inputOptions) {
}
