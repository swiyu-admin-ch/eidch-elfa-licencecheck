package ch.admin.astra.vz.lc.integration.verifierservice.client;

import ch.admin.astra.vz.lc.integration.verifierservice.exception.VerifierException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE;

@ExtendWith(MockitoExtension.class)
class RestErrorHandlerTest {

    @Mock
    private ClientHttpResponse response;

    @Mock
    private HttpRequest request;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final RestErrorHandler errorHandler = new RestErrorHandler(objectMapper);
    private final ThrowingCallable throwingCallable = () -> errorHandler.handle(request, response);

    @Test
    void handle_bodyReadThrowsIOException_throwsVerifierExceptionWithTechnicalFlag() throws IOException {
        // given
        given(response.getBody()).willThrow(new IOException("stream error"));

        // when
        final var exception = catchThrowableOfType(VerifierException.class, throwingCallable);

        // then
        assertThat(exception.getMessage()).startsWith("Could not read response.body stream error");
        assertThat(exception.getStatus()).isEqualTo(SERVICE_UNAVAILABLE);
        assertThat(exception.getIsBusinessError()).isFalse();
    }

    @Test
    void handle_emptyErrorBody_throwsVerifierExceptionWithTechnicalFlag() throws IOException {
        // given
        final var emptyJson = "";
        given(response.getBody()).willReturn(getInputStream(emptyJson));

        // when
        final var exception = catchThrowableOfType(VerifierException.class, throwingCallable);

        // then
        assertThat(exception.getStatus()).isEqualTo(HttpStatus.SERVICE_UNAVAILABLE);
        assertThat(exception.getMessage()).isEqualTo("Empty error body");
        assertThat(exception.getIsBusinessError()).isFalse();
    }

    @Test
    void handle_invalidErrorBody_throwsVerifierExceptionWithTechnicalFlag() throws IOException {
        // given
        final var invalidJson = "not-a-json";
        given(response.getBody()).willReturn(getInputStream(invalidJson));

        // when
        final var exception = catchThrowableOfType(VerifierException.class, throwingCallable);

        // then
        assertThat(exception.getStatus()).isEqualTo(HttpStatus.SERVICE_UNAVAILABLE);
        assertThat(exception.getMessage()).startsWith("Could not parse JSON <" + invalidJson + "> :");
        assertThat(exception.getIsBusinessError()).isFalse();
    }

    @Test
    void handle_missingDescriptionAndDetail_throwsVerifierExceptionWithTechnicalFlag() throws IOException {
        VerifierException exception;

        // given an erroneous JSON with missing description and detail
        final var errorJsonMissingDescriptionAndDetail = "{}";
        given(response.getBody()).willReturn(getInputStream(errorJsonMissingDescriptionAndDetail));
        given(response.getStatusCode()).willReturn(HttpStatus.SERVICE_UNAVAILABLE);

        // when
        exception = catchThrowableOfType(VerifierException.class, throwingCallable);

        // then
        assertThat(exception.getStatus()).isEqualTo(SERVICE_UNAVAILABLE);
        assertThat(exception.getMessage()).isEqualTo("Invalid error format: missing description or detail");
        assertThat(exception.getIsBusinessError()).isFalse();

        // given an erroneous JSON with missing detail
        final var errorJsonMissingDetail = "{\"error_description\":\"This is an error\"}";
        given(response.getBody()).willReturn(getInputStream(errorJsonMissingDetail));

        // when
        exception = catchThrowableOfType(VerifierException.class, throwingCallable);

        // then
        assertThat(exception.getStatus()).isEqualTo(SERVICE_UNAVAILABLE);
        assertThat(exception.getMessage()).isEqualTo("Description: null Detail: This is an error");
        assertThat(exception.getIsBusinessError()).isFalse();

        // given an erroneous JSON with missing description
        final var errorJsonMissingDescription = "{\"detail\":\"This is a detail\"}";
        given(response.getBody()).willReturn(getInputStream(errorJsonMissingDescription));

        // when
        exception = catchThrowableOfType(VerifierException.class, () -> errorHandler.handle(request, response));

        // then
        assertThat(exception.getStatus()).isEqualTo(SERVICE_UNAVAILABLE);
        assertThat(exception.getMessage()).isEqualTo("Description: This is a detail Detail: null");
        assertThat(exception.getIsBusinessError()).isFalse();
    }

    @Test
    void handle_businessError_throwsVerifierExceptionWithBusinessFlag() throws IOException {
        // given
        final var errorJson = "{\"detail\":\"The verification with the identifier '34234' was not found\"}";
        given(response.getBody()).willReturn(getInputStream(errorJson));
        given(response.getStatusCode()).willReturn(HttpStatus.NOT_FOUND);

        // when
        final var exception = catchThrowableOfType(VerifierException.class, () -> errorHandler.handle(request, response));

        // then
        assertThat(exception.getStatus()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(exception.getMessage()).isEqualTo("The verification with the identifier '34234' was not found");
        assertThat(exception.getIsBusinessError()).isTrue();
    }

    private static InputStream getInputStream(String input) {
        return new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
    }
}
