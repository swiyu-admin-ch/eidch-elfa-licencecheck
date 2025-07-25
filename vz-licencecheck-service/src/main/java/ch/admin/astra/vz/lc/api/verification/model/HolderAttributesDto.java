package ch.admin.astra.vz.lc.api.verification.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Singular;

import java.util.Map;

@Schema(name = "HolderAttributes")
public record HolderAttributesDto(@Singular Map<String, String> attributes) {
}
