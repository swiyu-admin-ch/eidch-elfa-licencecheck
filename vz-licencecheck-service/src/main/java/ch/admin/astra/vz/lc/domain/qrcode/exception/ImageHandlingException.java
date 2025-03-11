package ch.admin.astra.vz.lc.domain.qrcode.exception;

public class ImageHandlingException extends RuntimeException{

    public static final String EXCEPTION_MSG = "image-handling";

    public ImageHandlingException(String message, Throwable e) {
        super(message, e);
    }

    @Override
    public String toString() {
        return String.format("An error occurred. Could not generate QR-Code: %s", getMessage());
    }
}
