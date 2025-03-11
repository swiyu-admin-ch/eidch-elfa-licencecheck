package ch.admin.astra.vz.lc.api;

import ch.admin.astra.vz.lc.domain.vam.model.ErrorCodeDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Jacksonized
@Builder(toBuilder = true)
@Schema(name = "VerificationStatusResponse")
public class VerificationStatusResponse {

    private String id;

    private VerificationStatus status;

    private String verificationUrl;

    private ErrorCodeDto errorCode;

    private String errorDescription;

    private HolderResponse holderResponse;

}
