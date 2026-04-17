package ch.admin.astra.vz.lc.integration.verifierservice.client;

import ch.admin.astra.vz.controller.verifier.api.VerifierManagementApiApi;
import ch.admin.astra.vz.controller.verifier.model.CreateVerificationManagementDto;
import ch.admin.astra.vz.controller.verifier.model.ManagementResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

/**
 * OpenAPI-based implementation of {@link VerifierServiceClient}.
 * Uses the generated OpenAPI client (VerifierManagementApiApi) to communicate with the Verifier Service.
 */
@RequiredArgsConstructor
@Slf4j
public class OpenAPIVerifierServiceImpl implements VerifierServiceClient {

    private final VerifierManagementApiApi verifierManagementApiApi;

    @Override
    public ManagementResponseDto createVerification(CreateVerificationManagementDto createVerificationManagementDto) {
        log.debug("Creating verification via OpenAPI client");
        return verifierManagementApiApi.createVerification(createVerificationManagementDto);
    }

    @Override
    public ManagementResponseDto getVerificationStatus(UUID verificationId) {
        log.debug("Getting verification status for {} via OpenAPI client", verificationId);
         return verifierManagementApiApi.getVerification(verificationId);
    }
}

