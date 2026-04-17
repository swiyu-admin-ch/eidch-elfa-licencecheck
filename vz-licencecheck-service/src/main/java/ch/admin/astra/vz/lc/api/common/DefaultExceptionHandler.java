package ch.admin.astra.vz.lc.api.common;

import ch.admin.astra.vz.lc.api.common.model.ErrorResponseDto;
import ch.admin.astra.vz.lc.core.logging.LoggingService;
import ch.admin.astra.vz.lc.integration.verifiermanagement.exception.VerifierException;
import ch.admin.astra.vz.lc.modules.verification.exception.FileMappingException;
import ch.admin.astra.vz.lc.modules.verification.exception.FileStorageException;
import ch.admin.astra.vz.lc.modules.verification.exception.ImageHandlingException;
import io.micrometer.tracing.Tracer;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

import static org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE;

@RestControllerAdvice
@RequiredArgsConstructor
public class DefaultExceptionHandler {

    private final LoggingService loggingService;
    private final Tracer tracer;

    @ExceptionHandler(ImageHandlingException.class)
    @ResponseStatus(SERVICE_UNAVAILABLE)
    public ResponseEntity<ErrorResponseDto> handleImageHandlingException(ImageHandlingException ex, HttpServletRequest request) {
        loggingService.logException(ex);
        return buildResponse(ImageHandlingException.EXCEPTION_MSG, request, ex);
    }

    @ExceptionHandler(FileMappingException.class)
    @ResponseStatus(SERVICE_UNAVAILABLE)
    public ResponseEntity<ErrorResponseDto> handleFileMappingException(FileMappingException ex, HttpServletRequest request) {
        loggingService.logException(ex);
       return buildResponse(FileMappingException.EXCEPTION_MSG, request, ex);
    }

    @ExceptionHandler(FileStorageException.class)
    @ResponseStatus(SERVICE_UNAVAILABLE)
    public ResponseEntity<ErrorResponseDto> handleFileStorageException(FileStorageException ex, HttpServletRequest request) {
        loggingService.logException(ex);
       return buildResponse(FileStorageException.EXCEPTION_MSG, request, ex);
    }

    @ExceptionHandler(VerifierException.class)
    @ResponseStatus(SERVICE_UNAVAILABLE)
    public ResponseEntity<ErrorResponseDto> handleVerifierException(VerifierException ex, HttpServletRequest request) {
        loggingService.logApiException(ex, ex.getIsBusinessError());
       return buildResponse(VerifierException.EXCEPTION_MSG, request, ex);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public void handleException(Exception ex) {
        loggingService.logException(ex);
    }

    private ResponseEntity<ErrorResponseDto> buildResponse(String errorCode, HttpServletRequest request, Exception ex) {
        var traceId = getTraceId();
        var response = new ErrorResponseDto(
                errorCode,
                SERVICE_UNAVAILABLE.value(),
                ex.getMessage(),
                traceId,
                request.getRequestURI(),
                LocalDateTime.now());
        return ResponseEntity
                .status(SERVICE_UNAVAILABLE)
                .body(response);
    }
    
    @NotNull
    String getTraceId() {
        var currentSpan = this.tracer.currentSpan();
        return currentSpan != null ? currentSpan.context().traceId() : "N/A";
    }
}
