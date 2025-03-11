package ch.admin.astra.vz.lc.domain.qrcode;

import ch.admin.astra.vz.lc.domain.qrcode.exception.ImageHandlingException;
import ch.admin.astra.vz.lc.domain.qrcode.impl.QrCodeServiceImpl;
import ch.admin.astra.vz.lc.domain.qrcode.model.QrCode;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;

import static org.assertj.core.api.Assertions.assertThat;

class QrCodeServiceTest {

    private final QrCodeService qrCodeService = new QrCodeServiceImpl();

    @Test
    void testCreate_qrCodeContainsTestData_succeeds() throws Exception {
        String qrText = "test";
        QrCode qrCode = qrCodeService.create(qrText, 500);
        BufferedImage bi = ImageIO.read(new ByteArrayInputStream(qrCode.getImageData()));
        LuminanceSource lumSource = new BufferedImageLuminanceSource(bi);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(lumSource));
        Result decoded = new QRCodeReader().decode(bitmap);
        assertThat(decoded.getText()).isEqualTo(qrText);
    }

    @Test
    void testCreate_qrCodeContainsTestData_throwsError() {
        Exception exception = Assertions.assertThrows(Exception.class, () -> qrCodeService.create("test", -11));

        Assertions.assertInstanceOf(ImageHandlingException.class, exception);
    }

}
