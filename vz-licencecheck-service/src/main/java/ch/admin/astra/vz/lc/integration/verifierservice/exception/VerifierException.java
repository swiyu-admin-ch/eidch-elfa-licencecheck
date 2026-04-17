package ch.admin.astra.vz.lc.integration.verifierservice.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class VerifierException extends RuntimeException {

    public static final String EXCEPTION_MSG = "verifier";
    private final HttpStatus status;
    private final Boolean isBusinessError;

    public VerifierException(HttpStatus status, String message, Throwable e, Boolean isBusinessError) {
        super(message, e);
        this.status = status;
        this.isBusinessError = isBusinessError;
    }

    public VerifierException(HttpStatus status, String message, Boolean isBusinessError) {
        this(status, message, null, isBusinessError);
    }
}