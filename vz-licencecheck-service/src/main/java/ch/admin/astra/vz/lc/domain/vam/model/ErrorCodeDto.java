package ch.admin.astra.vz.lc.domain.vam.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Schema(name = "ErrorCode", enumAsRef = true)
public enum ErrorCodeDto {

    CREDENTIAL_INVALID("CREDENTIAL_INVALID"),

    JWT_EXPIRED("JWT_EXPIRED"),

    INVALID_FORMAT("INVALID_FORMAT"),

    CREDENTIAL_EXPIRED("CREDENTIAL_EXPIRED"),

    MISSING_NONCE("MISSING_NONCE"),

    UNSUPPORTED_FORMAT("UNSUPPORTED_FORMAT"),

    CREDENTIAL_REVOKED("CREDENTIAL_REVOKED"),

    CREDENTIAL_SUSPENDED("CREDENTIAL_SUSPENDED"),

    HOLDER_BINDING_MISMATCH("HOLDER_BINDING_MISMATCH"),

    CREDENTIAL_MISSING_DATA("CREDENTIAL_MISSING_DATA"),

    UNRESOLVABLE_STATUS_LIST("UNRESOLVABLE_STATUS_LIST"),

    CLIENT_REJECTED("CLIENT_REJECTED");

    private final String value;
}
