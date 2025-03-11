package ch.admin.astra.vz.lc.domain.qrcode.impl;

import ch.admin.astra.vz.lc.domain.qrcode.QrCodeService;
import ch.admin.astra.vz.lc.domain.qrcode.exception.ImageHandlingException;
import ch.admin.astra.vz.lc.domain.qrcode.model.QrCode;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;

@Service
public class QrCodeServiceImpl implements QrCodeService {

    private static final String FORMAT = "png";
    private static final String MIME = "image/png";

    public QrCode create(String data, int size) {
        try {
            BitMatrix matrix = new MultiFormatWriter().encode(data, BarcodeFormat.QR_CODE, size, size);
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(matrix, FORMAT, os);
            os.close();

            return QrCode.builder()
                .imageData(os.toByteArray())
                .format(FORMAT)
                .mime(MIME)
                .build();

        } catch (Exception e) {
            throw new ImageHandlingException(e.getMessage(), e);
        }
    }
}
