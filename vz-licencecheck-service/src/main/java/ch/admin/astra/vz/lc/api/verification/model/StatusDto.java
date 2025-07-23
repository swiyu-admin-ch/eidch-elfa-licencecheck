package ch.admin.astra.vz.lc.api.verification.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "Status", enumAsRef = true)
public enum StatusDto {
    PENDING,
    SUCCESS,
    FAILED
}
