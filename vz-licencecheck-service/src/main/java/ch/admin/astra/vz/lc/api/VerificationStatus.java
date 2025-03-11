package ch.admin.astra.vz.lc.api;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "VerificationStatus", enumAsRef = true)
public enum VerificationStatus {
    PENDING,
    SUCCESS,
    FAILED
}
