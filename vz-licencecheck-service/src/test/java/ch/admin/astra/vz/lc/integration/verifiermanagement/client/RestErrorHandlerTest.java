package ch.admin.astra.vz.lc.integration.verifiermanagement.client;

import ch.admin.astra.vz.lc.integration.verifiermanagement.client.model.ErrorDto;
import ch.admin.astra.vz.lc.integration.verifiermanagement.exception.VerifierException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE;

class RestErrorHandlerTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final RestErrorHandler errorHandler = new RestErrorHandler(objectMapper);

    @Test
    void handle_bodyReadThrowsIOException_throwsVerifierExceptionWithTechnicalFlag() throws IOException {
        RestErrorHandler errorHandler = new RestErrorHandler(new com.fasterxml.jackson.databind.ObjectMapper());
        ClientHttpResponse response = Mockito.mock(ClientHttpResponse.class);
        Mockito.when(response.getBody()).thenThrow(new IOException("stream error"));
        HttpRequest request = Mockito.mock(HttpRequest.class);

        VerifierException ex = assertThrows(VerifierException.class, () -> errorHandler.handle(request, response));
        assertThat(ex.getMessage()).startsWith("Could not read response.body stream error");
        assertThat(ex.getStatus()).isEqualTo(SERVICE_UNAVAILABLE);
        assertThat(ex.getIsBusinessError()).isFalse();
    }

    @Test
    void handle_emptyErrorBody_throwsVerifierExceptionWithTechnicalFlag() throws IOException {
        // arrange
        String emptyJson = "";
        ClientHttpResponse response = Mockito.mock(ClientHttpResponse.class);
        Mockito.when(response.getBody()).thenReturn(new ByteArrayInputStream(emptyJson.getBytes(StandardCharsets.UTF_8)));
        HttpRequest request = Mockito.mock(HttpRequest.class);

        // act
        VerifierException ex = assertThrows(VerifierException.class, () -> errorHandler.handle(request, response));

        // assert
        assertEquals(HttpStatus.SERVICE_UNAVAILABLE, ex.getStatus());
        assertThat(ex.getMessage()).isEqualTo("Empty error body");
        assertFalse(ex.getIsBusinessError());
    }

    @Test
    void handle_invalidErrorBody_throwsVerifierExceptionWithTechnicalFlag() throws IOException {
        String invalidJson = "not-a-json";
        ClientHttpResponse response = Mockito.mock(ClientHttpResponse.class);
        Mockito.when(response.getBody()).thenReturn(new ByteArrayInputStream(invalidJson.getBytes(StandardCharsets.UTF_8)));
        HttpRequest request = Mockito.mock(HttpRequest.class);

        VerifierException ex = assertThrows(VerifierException.class, () -> errorHandler.handle(request, response));

        assertEquals(HttpStatus.SERVICE_UNAVAILABLE, ex.getStatus());
        assertThat(ex.getMessage()).startsWith("Could not parse JSON <" + invalidJson + "> :");
        assertFalse(ex.getIsBusinessError());
    }

    @Test
    void handle_missingStatusOrDetail_throwsVerifierExceptionWithTechnicalFlag() throws IOException {
        // ErrorDto missing status
        String errorJsonMissingStatus = "{\"detail\":\"some-detail\"}";
        ClientHttpResponse responseMissingStatus = Mockito.mock(ClientHttpResponse.class);
        Mockito.when(responseMissingStatus.getBody())
                .thenReturn(new ByteArrayInputStream(errorJsonMissingStatus.getBytes(StandardCharsets.UTF_8)));
        HttpRequest request = Mockito.mock(HttpRequest.class);

        VerifierException ex1 = assertThrows(VerifierException.class, () -> errorHandler.handle(request, responseMissingStatus));
        assertEquals(SERVICE_UNAVAILABLE, ex1.getStatus());
        assertEquals("Invalid error format: missing status or detail", ex1.getMessage());
        assertFalse(ex1.getIsBusinessError());

        // ErrorDto missing detail
        String errorJsonMissingDetail = "{\"status\":\"NOT_FOUND\"}";
        ClientHttpResponse responseMissingDetail = Mockito.mock(ClientHttpResponse.class);
        Mockito.when(responseMissingDetail.getBody())
                .thenReturn(new ByteArrayInputStream(errorJsonMissingDetail.getBytes(StandardCharsets.UTF_8)));

        VerifierException ex2 = assertThrows(VerifierException.class, () -> errorHandler.handle(request, responseMissingDetail));
        assertEquals(SERVICE_UNAVAILABLE, ex2.getStatus());
        assertEquals("Invalid error format: missing status or detail", ex2.getMessage());
        assertFalse(ex2.getIsBusinessError());
    }

    @Test
    void handle_businessError_throwsVerifierExceptionWithBusinessFlag() throws IOException {
        ErrorDto errorDto = ErrorDto.builder()
                .status(HttpStatus.NOT_FOUND)
                .detail("verification-not-found")
                .build();
        String errorJson = objectMapper.writeValueAsString(errorDto);
        ClientHttpResponse response = Mockito.mock(ClientHttpResponse.class);
        Mockito.when(response.getBody()).thenReturn(new ByteArrayInputStream(errorJson.getBytes(StandardCharsets.UTF_8)));
        HttpRequest request = Mockito.mock(HttpRequest.class);

        VerifierException ex = assertThrows(VerifierException.class, () -> errorHandler.handle(request, response));

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatus());
        assertEquals("verification-not-found", ex.getMessage());
        assertTrue(ex.getIsBusinessError());
    }






}
