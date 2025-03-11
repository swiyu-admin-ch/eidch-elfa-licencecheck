package ch.admin.astra.vz.lc.domain.vam.service;

import ch.admin.astra.vz.lc.domain.vam.api.VerifierAgentManagementApi;
import ch.admin.astra.vz.lc.domain.vam.exception.VAMException;
import ch.admin.astra.vz.lc.domain.vam.model.CreateVerificationManagementDto;
import ch.admin.astra.vz.lc.domain.vam.model.ErrorDto;
import ch.admin.astra.vz.lc.domain.vam.model.ManagementResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.util.UUID;

@RequiredArgsConstructor
@Slf4j
public class VerifierAgentManagementImpl implements VerifierAgentManagementClient {

    private final VerifierAgentManagementApi verifierAgentManagementApi;
    private final ObjectMapper objectMapper;

    @Override
    public ManagementResponseDto createVerification(CreateVerificationManagementDto createVerificationManagementDto) {
        return callApi(verifierAgentManagementApi.createVerification(createVerificationManagementDto));
    }

    @Override
    public ManagementResponseDto getVerificationStatus(UUID verificationId) {
        return callApi(verifierAgentManagementApi.getVerification(verificationId));
    }

    /**
     * Makes a synchronous API call using the given VerifierAgentManagementApi object and returns the response.
     * If the response is successful, the body of the response is returned.
     * If the response is not successful, a {@link VAMException} is thrown.
     *
     * @param vamApi The VerifierAgentManagementApi object used to make the API call.
     * @return The body of the response if it is successful, null otherwise.
     * @throws VAMException If there is an error making the API call or the response is not successful.
     */
    private ManagementResponseDto callApi(Call<ManagementResponseDto> vamApi) {
        try {
            Response<ManagementResponseDto> response = vamApi.execute();
            if (response.isSuccessful()) {
                return response.body();
            }

            throw new VAMException(null, mapErrorToException(response));
        } catch (Exception e) {
            throw new VAMException(e, null);
        }
    }

    /**
     * Maps the error response from the Tech Adapter API to an exception errorCode.
     *
     * @param response The response from the Tech Adapter API.
     * @return exception errorCode.
     * @throws IOException If there is an error reading the error body.
     */
    private ErrorDto mapErrorToException(Response<ManagementResponseDto> response) throws IOException {
        try (ResponseBody errorBody = response.errorBody()) {
            assert errorBody != null;
            return objectMapper.readValue(errorBody.string(), ErrorDto.class);
        }
    }

}
