package ch.admin.astra.vz.lc.api.verification.model;

import ch.admin.astra.vz.lc.integration.verifiermanagement.client.model.VerificationErrorResponseCodeDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.NonNull;

@Builder(toBuilder = true)
@Schema(name = "VerificationState")
public record VerificationStateDto(@NonNull String id, @NonNull StatusDto status, String verificationUrl,
                                   VerificationErrorResponseCodeDto errorCode, String errorDescription,
                                   HolderAttributesDto holderAttributes) {
}
