package ch.admin.astra.vz.lc.api;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "ErrorResponse")
public record ErrorResponseDto(String errorCode, Integer status) {
}
