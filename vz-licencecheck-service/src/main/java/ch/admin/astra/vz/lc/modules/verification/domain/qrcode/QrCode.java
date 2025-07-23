package ch.admin.astra.vz.lc.modules.verification.domain.qrcode;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class QrCode {

    private final String format;
    private final String mime;
    private final byte[] imageData;

}
