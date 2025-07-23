package ch.admin.astra.vz.lc.integration.verifiermanagement.exception;

import ch.admin.astra.vz.lc.integration.verifiermanagement.client.model.ErrorDto;
import lombok.Getter;

@Getter
public class VAMException extends RuntimeException {

    public static final String EXCEPTION_MSG = "vam";

    private final transient ErrorDto error;

    public VAMException(Throwable exception, ErrorDto error) {
        // exception if there is an issue during the mapping of the error
        super(exception);
        // error from the vam
        this.error = error;
    }

    @Override
    public String toString() {
        String message = this.getCause() != null ? getMessage() : error.getDetail();
            return String.format("An error occurred. Error while accessing the VAM service: %s", message);
    }
}
