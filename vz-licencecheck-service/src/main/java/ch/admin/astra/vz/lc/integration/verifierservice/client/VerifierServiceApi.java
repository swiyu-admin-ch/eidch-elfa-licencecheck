package ch.admin.astra.vz.lc.integration.verifierservice.client;

import ch.admin.astra.vz.lc.integration.verifierservice.client.model.CreateVerificationManagementDto;
import ch.admin.astra.vz.lc.integration.verifierservice.client.model.ManagementResponseDto;
import org.springframework.web.client.RestClient;

import java.util.UUID;

public class VerifierServiceApi implements VerifierApi {

    public static final String CREATE_VERIFICATIONS = "/management/api/verifications";
    public static final String GET_VERIFICATIONS = "/management/api/verifications/{verificationId}";

    private final RestClient restClient;

    public VerifierServiceApi(RestClient restClient) {
        this.restClient = restClient;
    }

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
        return restClient.get()
                .uri(GET_VERIFICATIONS, verificationId)
                .retrieve()
                .body(ManagementResponseDto.class);
    }

}
