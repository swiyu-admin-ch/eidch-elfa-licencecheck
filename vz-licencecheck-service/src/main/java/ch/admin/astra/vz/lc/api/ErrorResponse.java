package ch.admin.astra.vz.lc.api;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "ErrorResponse")
public record ErrorResponse(String errorCode, Integer status) {}
