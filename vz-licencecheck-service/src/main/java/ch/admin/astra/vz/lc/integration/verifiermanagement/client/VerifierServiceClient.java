package ch.admin.astra.vz.lc.integration.verifiermanagement.client;

import ch.admin.astra.vz.lc.integration.verifiermanagement.client.model.CreateVerificationManagementDto;
import ch.admin.astra.vz.lc.integration.verifiermanagement.client.model.ManagementResponseDto;

import java.util.UUID;

/**
 * Interface client to connect to the Verifier Service.
 */
public interface VerifierServiceClient {

    ManagementResponseDto createVerification(CreateVerificationManagementDto createVerificationManagementDto);

    ManagementResponseDto getVerificationStatus(UUID verificationId);

}
