package ch.admin.astra.vz.lc.integration.verifierservice.client;

import ch.admin.astra.vz.lc.integration.verifierservice.client.model.CreateVerificationManagementDto;
import ch.admin.astra.vz.lc.integration.verifierservice.client.model.ManagementResponseDto;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

public interface VerifierApi {

    /**
     * Creates a new verification process with the given attributes
     *
     * @param createVerificationManagementDto (required)
     * @return Call<ManagementResponseDto>
     */
    ManagementResponseDto createVerification(
            @RequestBody CreateVerificationManagementDto createVerificationManagementDto
    );

    /**
     * @param verificationId (required)
     * @return Call<ManagementResponseDto>
     */
    ManagementResponseDto getVerification(
            @PathVariable("verificationId") UUID verificationId
    );

}
