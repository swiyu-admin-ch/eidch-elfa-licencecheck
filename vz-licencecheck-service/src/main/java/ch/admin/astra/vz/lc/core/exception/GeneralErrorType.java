package ch.admin.astra.vz.lc.core.exception;

import ch.admin.astra.vz.commons.error.exception.ErrorType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum GeneralErrorType implements ErrorType {
    UNEXPECTED_ERROR("Internal server error", "An unexpected error occurred. Please try again later.", HttpStatus.INTERNAL_SERVER_ERROR),
    VERIFIER_SERVICE_ERROR("Verifier service error", "An error occurred while communicating with the verifier service.", HttpStatus.SERVICE_UNAVAILABLE),
    IMAGE_HANDLING_ERROR("Image handling error", "An error occurred while processing an image.", HttpStatus.SERVICE_UNAVAILABLE),
    FILE_MAPPING_ERROR("File mapping error", "An error occurred while mapping a file.", HttpStatus.SERVICE_UNAVAILABLE),
    FILE_STORAGE_ERROR("File storage error", "An error occurred while storing a file.", HttpStatus.SERVICE_UNAVAILABLE),
    USE_CASE_NOT_FOUND("Use case not found", "The requested use case could not be found.", HttpStatus.BAD_REQUEST);

    private final String message;
    private final String detail;
    private final HttpStatus httpStatus;

    @Override
    public String getName() {
        return name();
    }
}