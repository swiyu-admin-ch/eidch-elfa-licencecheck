package ch.admin.astra.vz.lc.modules.verification.exception;

import lombok.Getter;

@Getter
public class FileStorageException extends RuntimeException {

    public static final String EXCEPTION_MSG = "file-storage";


    public FileStorageException(Throwable exception) {
        super(exception);
    }

    @Override
    public String toString() {
        return String.format("An error occurred. Error while reading use-case directory: %s", getMessage());
    }
}
