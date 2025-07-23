package ch.admin.astra.vz.lc.api.verification.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

@Schema(name = "StartVerification")
public record StartVerificationDto(@NotNull UUID useCaseId) {
}
