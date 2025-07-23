package ch.admin.astra.vz.lc.api.common.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "ErrorResponse")
public record ErrorResponseDto(String errorCode, Integer status) {
}
