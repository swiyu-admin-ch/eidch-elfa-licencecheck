package ch.admin.astra.vz.lc.api.common.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(name = "FeatureFlags")
public record FeatureFlagsDto(

    @NotNull
    @Schema(description = "Whether MDL functionality is enabled")
    boolean enableMdl,

    @NotNull
    @Schema(description = "Whether Verifier Service integration uses OpenApi Generator")
    boolean generatedVerifierClient


){}
