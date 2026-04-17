package ch.admin.astra.vz.lc.api.common;

import ch.admin.astra.vz.commons.error.exception.ErrorType;
import ch.admin.astra.vz.commons.mapper.EnumMapper;
import ch.admin.astra.vz.lc.api.common.model.ErrorResponseDto;
import ch.admin.astra.vz.lc.api.common.model.ErrorTypeDto;
import ch.admin.astra.vz.lc.core.logging.LoggingService;
import ch.admin.astra.vz.lc.integration.verifierservice.exception.VerifierException;
import ch.admin.astra.vz.lc.modules.verification.exception.FileMappingException;
import ch.admin.astra.vz.lc.modules.verification.exception.FileStorageException;
import ch.admin.astra.vz.lc.modules.verification.exception.ImageHandlingException;
import ch.admin.astra.vz.lc.modules.verification.exception.UseCaseNotFoundException;
import io.micrometer.tracing.Tracer;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

import static ch.admin.astra.vz.lc.core.exception.GeneralErrorType.*;

@RestControllerAdvice
@RequiredArgsConstructor
public class DefaultExceptionHandler extends ResponseEntityExceptionHandler {

    private final LoggingService loggingService;
    private final Tracer tracer;

    @ExceptionHandler(ImageHandlingException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public ResponseEntity<ErrorResponseDto> handleImageHandlingException(ImageHandlingException ex, HttpServletRequest request) {
        loggingService.logException(ex);
        return buildResponse(IMAGE_HANDLING_ERROR, request);
    }

    @ExceptionHandler(FileMappingException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public ResponseEntity<ErrorResponseDto> handleFileMappingException(FileMappingException ex, HttpServletRequest request) {
        loggingService.logException(ex);
        return buildResponse(FILE_MAPPING_ERROR, request);
    }

    @ExceptionHandler(FileStorageException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public ResponseEntity<ErrorResponseDto> handleFileStorageException(FileStorageException ex, HttpServletRequest request) {
        loggingService.logException(ex);
        return buildResponse(FILE_STORAGE_ERROR, request);
    }

    @ExceptionHandler(UseCaseNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponseDto> handleUseCaseNotFoundException(UseCaseNotFoundException ex, HttpServletRequest request) {
        loggingService.logException(ex);
        return buildResponse(USE_CASE_NOT_FOUND, request);
    }

    @ExceptionHandler(VerifierException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public ResponseEntity<ErrorResponseDto> handleVerifierException(VerifierException ex, HttpServletRequest request) {
        loggingService.logException(ex);
        return buildResponse(VERIFIER_SERVICE_ERROR, request);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorResponseDto> handleException(Exception ex, HttpServletRequest request) {
        loggingService.logException(ex);
        return buildResponse(UNEXPECTED_ERROR, request);
    }

    private ResponseEntity<ErrorResponseDto> buildResponse(ErrorType errorType, HttpServletRequest request) {
        var response = new ErrorResponseDto(
                EnumMapper.mapEnum(ErrorTypeDto.class, errorType.getName()),
                errorType.getMessage(),
                errorType.getDetail(),
                getTraceId(),
                request.getRequestURI(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(errorType.getHttpStatus()).body(response);
    }

    @NotNull
    String getTraceId() {
        var currentSpan = this.tracer.currentSpan();
        return currentSpan != null ? currentSpan.context().traceId() : "N/A";
    }
}