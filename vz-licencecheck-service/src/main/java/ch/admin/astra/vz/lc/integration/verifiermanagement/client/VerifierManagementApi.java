package ch.admin.astra.vz.lc.integration.verifiermanagement.client;

import ch.admin.astra.vz.lc.integration.verifiermanagement.client.model.CreateVerificationManagementDto;
import ch.admin.astra.vz.lc.integration.verifiermanagement.client.model.ManagementResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.client.RestClient;

import java.util.UUID;

@Deprecated
@RequiredArgsConstructor
public class VerifierManagementApi implements VerifierApi {

    public static final String CREATE_VERIFICATIONS = "api/v1/verifications";
    public static final String GET_VERIFICATIONS = "api/v1/verifications/{verificationId}";

    private final RestClient restClient;

    @Override
    public ManagementResponseDto createVerification(CreateVerificationManagementDto createVerificationManagementDto) {
        return restClient.post()
                .uri(CREATE_VERIFICATIONS)
                .body(createVerificationManagementDto)
                .retrieve()
                .body(ManagementResponseDto.class);
    }

    @Override
    public ManagementResponseDto getVerification(UUID verificationId) {
        return restClient.post()
                .uri(GET_VERIFICATIONS, verificationId)
                .retrieve()
                .body(ManagementResponseDto.class);
    }

}
