package ch.admin.astra.vz.lc.api;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import java.util.UUID;

@Data
@Jacksonized
@Builder(toBuilder = true)
@Schema(name = "VerificationBeginResponse")
public class VerificationBeginResponse {

    private UUID id;

    private byte[] qrCode;

    private String qrCodeFormat;

}
