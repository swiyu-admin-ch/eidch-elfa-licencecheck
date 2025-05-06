package ch.admin.astra.vz.lc.api;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.NonNull;

import java.util.UUID;

@Schema(name = "VerificationStarted")
public record VerificationStartedDto(@NonNull UUID id, byte[] qrCode, String qrCodeFormat) {
}
