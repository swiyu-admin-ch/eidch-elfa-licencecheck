package ch.admin.astra.vz.lc.api.common.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "ErrorType", enumAsRef = true)
public enum ErrorTypeDto {
    UNEXPECTED_ERROR,
    VERIFIER_SERVICE_ERROR,
    IMAGE_HANDLING_ERROR,
    FILE_MAPPING_ERROR,
    FILE_STORAGE_ERROR,
    USE_CASE_NOT_FOUND
}