package ch.admin.astra.vz.lc.integration.verifiermanagement.client;

import ch.admin.astra.vz.lc.integration.verifiermanagement.client.model.CreateVerificationManagementDto;
import ch.admin.astra.vz.lc.integration.verifiermanagement.client.model.ErrorDto;
import ch.admin.astra.vz.lc.integration.verifiermanagement.client.model.ManagementResponseDto;
import ch.admin.astra.vz.lc.integration.verifiermanagement.exception.VerifierException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.ResponseBody;
import org.springframework.http.HttpStatus;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.util.UUID;

import static org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE;

@RequiredArgsConstructor
@Slf4j
public class VerifierServiceImpl implements VerifierServiceClient {

    private final VerifierApi verifierApi;
    private final ObjectMapper objectMapper;

    @Override
    public ManagementResponseDto createVerification(CreateVerificationManagementDto createVerificationManagementDto) {
        return callApi(verifierApi.createVerification(createVerificationManagementDto));
    }

    @Override
    public ManagementResponseDto getVerificationStatus(UUID verificationId) {
        return callApi(verifierApi.getVerification(verificationId));
    }

    /**
     * Makes a synchronous API call using the given VerifierApi object and returns the response.
     * If the response is successful, the body of the response is returned.
     * If the response is not successful, a {@link VerifierException} is thrown.
     *
     * @param verifierApi The VerifierManagementApi object used to make the API call.
     * @return The body of the response if it is successful, null otherwise.
     * @throws VerifierException If there is an error making the API call or the response is not successful.
     */
    private ManagementResponseDto callApi(Call<ManagementResponseDto> verifierApi) {
        try {
            Response<ManagementResponseDto> response = verifierApi.execute();
            if (response.isSuccessful()) {
                return response.body();
            }

            String errorBodyStr = null;
            try (ResponseBody errorBody = response.errorBody()) {
                errorBodyStr = (errorBody != null) ? errorBody.string() : null;
            }

            try {
                ErrorDto error = mapErrorFromString(errorBodyStr);
                // consider verifier-service response errors as business errors and log as debug
                // for example, verification-not-found
                throw new VerifierException(error.getStatus(), error.getDetail(), true);
            } catch (IOException mappingEx) {
                // other errors are considered more serious and logged as error
                // for example, verifier-service not reachable
                throw new VerifierException(SERVICE_UNAVAILABLE, errorBodyStr, false);
            }
        } catch (IOException e) {
            throw new VerifierException(SERVICE_UNAVAILABLE, e.getMessage(), false);
        }
    }

    private ErrorDto mapErrorFromString(String json) throws IOException {
        if (json == null || json.isBlank()) {
            throw new IOException("Empty error body");
        }

        var error = objectMapper.readValue(json, ErrorDto.class);

        // if not successfull in mapping all verifier-service ErrorDto fields, it's not considered as business error from verifier-service
        if(error.getStatus() == null || error.getDetail() == null) {
            throw new IOException("Invalid error format: missing status or detail");
        }
        return error;

    }

}
