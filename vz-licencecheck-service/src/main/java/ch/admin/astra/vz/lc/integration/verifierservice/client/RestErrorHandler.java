package ch.admin.astra.vz.lc.integration.verifierservice.client;

import ch.admin.astra.vz.lc.integration.verifierservice.client.model.ErrorDto;
import ch.admin.astra.vz.lc.integration.verifierservice.exception.VerifierException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.RestClient.ResponseSpec.ErrorHandler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE;

@Slf4j
@RequiredArgsConstructor
public class RestErrorHandler implements ErrorHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void handle(@NotNull HttpRequest request, ClientHttpResponse response) {
        String errorBodyJson = null;
        try {
            errorBodyJson = new String(response.getBody().readAllBytes(), StandardCharsets.UTF_8);

            if (errorBodyJson.isBlank()) {
                // error is considered more serious and logged as error
                throw new VerifierException(SERVICE_UNAVAILABLE, "Empty error body", false);
            }

            ErrorDto error = objectMapper.readValue(errorBodyJson, ErrorDto.class);
            if (error.getStatus() == null || error.getDetail() == null) {
                // error is considered more serious and logged as error
                throw new VerifierException(SERVICE_UNAVAILABLE, "Invalid error format: missing status or detail", false);
            }

            // error is considered less serious as business errors and logged as debug
            // for example, verification-not-found
            throw new VerifierException(error.getStatus(), error.getDetail(), true);

        } catch (IOException e) {
            String msg = (errorBodyJson == null)
                    ? "Could not read response.body " + e.getMessage()
                    : "Could not parse JSON <" + errorBodyJson + "> :" + e.getMessage();
            throw new VerifierException(SERVICE_UNAVAILABLE, msg, e, false);
        }
    }

}