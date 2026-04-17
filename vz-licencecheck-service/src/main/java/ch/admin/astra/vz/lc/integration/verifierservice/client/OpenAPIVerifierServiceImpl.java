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
    private final ch.admin.astra.vz.lc.integration.verifierservice.client.mapper.VerifierServiceModelMapper mapper;

    @Override
    public ch.admin.astra.vz.lc.integration.verifierservice.client.model.ManagementResponseDto createVerification(ch.admin.astra.vz.lc.integration.verifierservice.client.model.CreateVerificationManagementDto createVerificationManagementDto) {
        log.debug("Creating verification via OpenAPI client");
        CreateVerificationManagementDto request = mapper.toOpenApiModel(createVerificationManagementDto);
        ManagementResponseDto response = verifierManagementApiApi.createVerification(request);
        return mapper.toDomainDto(response);
    }

    @Override
    public ch.admin.astra.vz.lc.integration.verifierservice.client.model.ManagementResponseDto getVerificationStatus(UUID verificationId) {
        log.debug("Getting verification status for {} via OpenAPI client", verificationId);
        ManagementResponseDto response = verifierManagementApiApi.getVerification(verificationId);
        return mapper.toDomainDto(response);
    }
}

