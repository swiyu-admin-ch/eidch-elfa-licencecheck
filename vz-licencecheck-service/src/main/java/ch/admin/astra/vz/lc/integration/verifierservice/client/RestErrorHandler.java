package ch.admin.astra.vz.lc.integration.verifierservice.client;

import ch.admin.astra.vz.lc.integration.verifierservice.exception.VerifierException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
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
            HttpStatusCode responseStatus = response.getStatusCode();
            errorBodyJson = new String(response.getBody().readAllBytes(), StandardCharsets.UTF_8);

            if (errorBodyJson.isBlank()) {
                // error is considered more serious and logged as error
                throw new VerifierException(SERVICE_UNAVAILABLE, "Empty error body", false);
            }

            // TODO: in future verifier versions the response format will be more mature and mapping will be cleaner
            // ApiErrorDtoDto error = objectMapper.readValue(errorBodyJson, ApiErrorDtoDto.class);

            JsonNode error = objectMapper.readTree(errorBodyJson);

            if (!error.hasNonNull("detail") && !error.hasNonNull("error_description")) {
                // error is considered more serious and logged as error
                throw new VerifierException(SERVICE_UNAVAILABLE, "Invalid error format: missing description or detail", false);
            }

            // error is considered less serious as business errors and logged as debug
            // for example, verification-not-found
            var detail = error.hasNonNull("detail") ? error.get("detail").asText() : null;
            var description = error.hasNonNull("error_description") ? error.get("error_description").asText() : null;

            String templateRegex =
                    "The verification with the identifier '(.+?)' was not found";

            if (detail != null && detail.matches(templateRegex)) {
                throw new VerifierException(HttpStatus.resolve(responseStatus.value()), detail, true);
            } else {
                throw new VerifierException(HttpStatus.resolve(responseStatus.value()), String.format("Description: %s Detail: %s", detail, description), false);
            }
        } catch (IOException e) {
            String msg = (errorBodyJson == null)
                    ? "Could not read response.body " + e.getMessage()
                    : "Could not parse JSON <" + errorBodyJson + "> :" + e.getMessage();
            throw new VerifierException(SERVICE_UNAVAILABLE, msg, e, false);
        }
    }

}