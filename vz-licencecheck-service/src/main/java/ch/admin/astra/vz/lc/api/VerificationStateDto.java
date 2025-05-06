package ch.admin.astra.vz.lc.api;

import ch.admin.astra.vz.lc.domain.vam.model.ErrorCodeDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.NonNull;

@Builder(toBuilder = true)
@Schema(name = "VerificationState")
public record VerificationStateDto(@NonNull String id, @NonNull StatusDto status, String verificationUrl,
                                   ErrorCodeDto errorCode, String errorDescription,
                                   HolderAttributesDto holderAttributes) {
}
