package ch.admin.astra.vz.lc.domain.vam.service;

import ch.admin.astra.vz.lc.domain.vam.model.CreateVerificationManagementDto;
import ch.admin.astra.vz.lc.domain.vam.model.ManagementResponseDto;

import java.util.UUID;

/**
 * Interface client to connect to the Verifier Agent Management.
 */
public interface VerifierAgentManagementClient {


    ManagementResponseDto createVerification(CreateVerificationManagementDto createVerificationManagementDto);

    ManagementResponseDto getVerificationStatus(UUID verificationId);

}
