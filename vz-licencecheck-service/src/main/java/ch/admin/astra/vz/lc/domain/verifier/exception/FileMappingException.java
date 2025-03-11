package ch.admin.astra.vz.lc.domain.verifier.exception;

import lombok.Getter;

@Getter
public class FileMappingException extends RuntimeException {

    public static final String EXCEPTION_MSG = "file-mapping";

    private final String fileName;

    public FileMappingException(Throwable exception, String fileName) {
        super(exception);
        this.fileName = fileName;
    }

    @Override
    public String toString() {
        return String.format("An error occurred. Could not map use case file: %s Details: %s", fileName, getMessage());
    }
}
