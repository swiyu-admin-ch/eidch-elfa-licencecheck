package ch.admin.astra.vz.lc.api;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(name = "AppConfig")
public record AppConfigDto(

    @NotNull
    String version,

    @NotNull
    String environment

){}
