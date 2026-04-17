package ch.admin.astra.vz.lc.api.common.model;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(name = "ErrorResponse")
public record ErrorResponseDto(
    @Schema(example = "VERIFIER_SERVICE_ERROR", description = "Type of error that occurred")
    ErrorTypeDto errorType,

    @Schema(example = "Verifier service error", description = "Human-readable error message")
    String message,

    @Schema(example = "An error occurred while communicating with the verifier service.", description = "Detailed error description")
    String detail,

    @Schema(example = "abc123-def456-ghi789", description = "Unique trace ID for error tracking")
    String traceId,

    @Schema(example = "/api/v1/xxxx", description = "API path where the error occurred")
    String path,

    @Schema(example = "2025-09-04T14:30:45.123", description = "Timestamp when the error occurred")
    LocalDateTime timestamp
) {}
