package ch.admin.astra.vz.lc.api.verification.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

@Schema(name = "VerificationBeginResponse")
public record VerificationBeginResponseDto(@NotNull UUID id,
                                           @NotNull byte[] qrCode,
                                           @NotNull String qrCodeFormat) {
}