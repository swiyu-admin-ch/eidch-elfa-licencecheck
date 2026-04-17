package ch.admin.astra.vz.lc.api.common.model;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(name = "ErrorResponse")
public record ErrorResponseDto(
        String errorCode,
        Integer status,
        String message,
        String traceId,
        String requestUri,
        LocalDateTime timestamp) {
}
