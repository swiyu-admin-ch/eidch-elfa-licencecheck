package ch.admin.astra.vz.lc.domain.qrcode;

import ch.admin.astra.vz.lc.domain.qrcode.exception.ImageHandlingException;
import ch.admin.astra.vz.lc.domain.qrcode.model.QrCode;

/**
 * Service interface for generating QR codes.
 */
public interface QrCodeService {

    /**
     * Creates a {@link QrCode} from given inputs.
     *
     * @param data to convert to an image
     * @param size image size
     * @return {@link QrCode} containing the image, mime-type and format
     * @throws ImageHandlingException when an unexpected error occurs during conversion
     */
    QrCode create(String data, int size);

}
