package ch.admin.astra.vz.lc.integration.verifierservice.exception;

import ch.admin.astra.vz.commons.error.exception.ExternalServiceException;
import org.springframework.http.HttpStatus;

public class VerifierException extends ExternalServiceException {
      public VerifierException(HttpStatus status, String message, Throwable cause, Boolean isBusinessError) {
        super(status, message, cause, isBusinessError);
    }

    public VerifierException(HttpStatus status, String message, Boolean isBusinessError) {
        super(status, message, isBusinessError);
    }
}