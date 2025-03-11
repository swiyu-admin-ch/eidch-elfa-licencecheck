package ch.admin.astra.vz.lc.controller.advice;

import ch.admin.astra.vz.lc.api.ErrorResponse;
import ch.admin.astra.vz.lc.domain.qrcode.exception.ImageHandlingException;
import ch.admin.astra.vz.lc.domain.vam.exception.VAMException;
import ch.admin.astra.vz.lc.domain.verifier.exception.FileMappingException;
import ch.admin.astra.vz.lc.domain.verifier.exception.FileStorageException;
import ch.admin.astra.vz.lc.logging.LoggingService;
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
    public ResponseEntity<ErrorResponse> handleImageHandlingException(ImageHandlingException ex) {
        loggingService.logException(ex);
        return new ResponseEntity<>(new ErrorResponse(ImageHandlingException.EXCEPTION_MSG, HttpStatus.SERVICE_UNAVAILABLE.value()), HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(FileMappingException.class)
    public ResponseEntity<ErrorResponse> handleFileMappingException(FileMappingException ex) {
        loggingService.logException(ex);
        return new ResponseEntity<>(new ErrorResponse(FileMappingException.EXCEPTION_MSG, HttpStatus.SERVICE_UNAVAILABLE.value()), HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(FileStorageException.class)
    public ResponseEntity<ErrorResponse> handleFileStorageException(FileStorageException ex) {
        loggingService.logException(ex);
        return new ResponseEntity<>(new ErrorResponse(FileStorageException.EXCEPTION_MSG, HttpStatus.SERVICE_UNAVAILABLE.value()), HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(VAMException.class)
    public ResponseEntity<ErrorResponse> handleVAMException(VAMException ex) {
        loggingService.logException(ex);
        return new ResponseEntity<>(new ErrorResponse(VAMException.EXCEPTION_MSG, HttpStatus.SERVICE_UNAVAILABLE.value()), HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public void handleException(Exception ex) {
        loggingService.logException(ex);
    }
}
