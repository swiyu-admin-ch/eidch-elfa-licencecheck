package ch.admin.astra.vz.lc.integration.verifiermanagement.client;

import ch.admin.astra.vz.lc.integration.verifiermanagement.client.model.CreateVerificationManagementDto;
import ch.admin.astra.vz.lc.integration.verifiermanagement.client.model.ManagementResponseDto;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

import java.util.UUID;

public interface VerifierServiceApi extends VerifierApi {

    /**
     * Creates a new verification process with the given attributes
     *
     * @param createVerificationManagementDto (required)
     * @return Call<ManagementResponseDto>
     */
    @Headers({"Content-Type:application/json"})
    @POST("/management/api/verifications")
    Call<ManagementResponseDto> createVerification(
            @retrofit2.http.Body CreateVerificationManagementDto createVerificationManagementDto
    );

    /**
     * @param verificationId (required)
     * @return Call<ManagementResponseDto>
     */
    @GET("/management/api/verifications/{verificationId}")
    Call<ManagementResponseDto> getVerification(
            @retrofit2.http.Path("verificationId") UUID verificationId
    );

}
