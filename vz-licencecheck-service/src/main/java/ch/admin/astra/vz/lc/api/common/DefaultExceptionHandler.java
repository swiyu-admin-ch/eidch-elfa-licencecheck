package ch.admin.astra.vz.lc.api.common;

import ch.admin.astra.vz.lc.api.common.model.ErrorResponseDto;
import ch.admin.astra.vz.lc.core.logging.LoggingService;
import ch.admin.astra.vz.lc.integration.verifiermanagement.exception.VAMException;
import ch.admin.astra.vz.lc.modules.verification.exception.FileMappingException;
import ch.admin.astra.vz.lc.modules.verification.exception.FileStorageException;
import ch.admin.astra.vz.lc.modules.verification.exception.ImageHandlingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@RequiredArgsConstructor
public class DefaultExceptionHandler {

    private final LoggingService loggingService;

    @ExceptionHandler(ImageHandlingException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public ResponseEntity<ErrorResponseDto> handleImageHandlingException(ImageHandlingException ex) {
        loggingService.logException(ex);
        return new ResponseEntity<>(new ErrorResponseDto(ImageHandlingException.EXCEPTION_MSG, HttpStatus.SERVICE_UNAVAILABLE.value()), HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(FileMappingException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public ResponseEntity<ErrorResponseDto> handleFileMappingException(FileMappingException ex) {
        loggingService.logException(ex);
        return new ResponseEntity<>(new ErrorResponseDto(FileMappingException.EXCEPTION_MSG, HttpStatus.SERVICE_UNAVAILABLE.value()), HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(FileStorageException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public ResponseEntity<ErrorResponseDto> handleFileStorageException(FileStorageException ex) {
        loggingService.logException(ex);
        return new ResponseEntity<>(new ErrorResponseDto(FileStorageException.EXCEPTION_MSG, HttpStatus.SERVICE_UNAVAILABLE.value()), HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(VAMException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public ResponseEntity<ErrorResponseDto> handleVAMException(VAMException ex) {
        loggingService.logException(ex);
        return new ResponseEntity<>(new ErrorResponseDto(VAMException.EXCEPTION_MSG, HttpStatus.SERVICE_UNAVAILABLE.value()), HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public void handleException(Exception ex) {
        loggingService.logException(ex);
    }
}
