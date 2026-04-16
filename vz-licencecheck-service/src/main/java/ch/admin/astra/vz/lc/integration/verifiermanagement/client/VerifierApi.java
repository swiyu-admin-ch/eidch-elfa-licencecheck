package ch.admin.astra.vz.lc.integration.verifiermanagement.client;

import ch.admin.astra.vz.lc.integration.verifiermanagement.client.model.CreateVerificationManagementDto;
import ch.admin.astra.vz.lc.integration.verifiermanagement.client.model.ManagementResponseDto;
import retrofit2.Call;

import java.util.UUID;

public interface VerifierApi {

    /**
     * Creates a new verification process with the given attributes
     *
     * @param createVerificationManagementDto (required)
     * @return Call<ManagementResponseDto>
     */
    Call<ManagementResponseDto> createVerification(
            @retrofit2.http.Body CreateVerificationManagementDto createVerificationManagementDto
    );

    /**
     * @param verificationId (required)
     * @return Call<ManagementResponseDto>
     */
    Call<ManagementResponseDto> getVerification(
            @retrofit2.http.Path("verificationId") UUID verificationId
    );

}
