package ch.admin.astra.vz.lc.integration.verifierservice.client;

import ch.admin.astra.vz.controller.verifier.model.CreateVerificationManagementDto;
import ch.admin.astra.vz.controller.verifier.model.ManagementResponseDto;

import java.util.UUID;

/**
 * Interface client to connect to the Verifier Service.
 */
public interface VerifierServiceClient {

    ManagementResponseDto createVerification(CreateVerificationManagementDto createVerificationManagementDto);

    ManagementResponseDto getVerificationStatus(UUID verificationId);

}
