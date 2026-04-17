package ch.admin.astra.vz.lc.integration.verifierservice.client;

import ch.admin.astra.vz.lc.integration.verifierservice.client.model.CreateVerificationManagementDto;
import ch.admin.astra.vz.lc.integration.verifierservice.client.model.ManagementResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@RequiredArgsConstructor
@Slf4j
public class VerifierServiceImpl implements VerifierServiceClient {

    private final VerifierApi verifierApi;

    @Override
    public ManagementResponseDto createVerification(CreateVerificationManagementDto createVerificationManagementDto) {
        return verifierApi.createVerification(createVerificationManagementDto);
    }

    @Override
    public ManagementResponseDto getVerificationStatus(UUID verificationId) {
        return verifierApi.getVerification(verificationId);
    }

}
