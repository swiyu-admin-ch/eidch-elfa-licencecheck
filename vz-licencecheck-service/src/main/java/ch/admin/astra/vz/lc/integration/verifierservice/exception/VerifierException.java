package ch.admin.astra.vz.lc.integration.verifierservice.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class VerifierException extends RuntimeException {

    public static final String EXCEPTION_MSG = "verifier";
    HttpStatus status;
    Boolean isBusinessError;
    public VerifierException(HttpStatus status, String message, Boolean isBusinessError) {
        super(message);
        this.status = status;
        this.isBusinessError = isBusinessError;
    }
}