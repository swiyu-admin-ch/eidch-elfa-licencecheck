package ch.admin.astra.vz.lc.integration.verifierservice.client;

import ch.admin.astra.vz.lc.integration.verifierservice.exception.VerifierException;
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
    void handle_missingDescriptionAndDetail_throwsVerifierExceptionWithTechnicalFlag() throws IOException {
        // Error JSON missing both description and detail
        String errorJsonMissingDescriptionAndDetail = "{}";
        ClientHttpResponse responseMissingStatus = Mockito.mock(ClientHttpResponse.class);
        Mockito.when(responseMissingStatus.getBody())
                .thenReturn(new ByteArrayInputStream(errorJsonMissingDescriptionAndDetail.getBytes(StandardCharsets.UTF_8)));
        Mockito.when(responseMissingStatus.getStatusCode()).thenReturn(HttpStatus.SERVICE_UNAVAILABLE);
        HttpRequest request = Mockito.mock(HttpRequest.class);

        VerifierException ex1 = assertThrows(VerifierException.class, () -> errorHandler.handle(request, responseMissingStatus));
        assertEquals(SERVICE_UNAVAILABLE, ex1.getStatus());
        assertEquals("Invalid error format: missing description or detail", ex1.getMessage());
        assertFalse(ex1.getIsBusinessError());

        // Error JSON missing detail
        String errorJsonMissingDetail = "{\"error_description\":\"This is an error\"}";
        ClientHttpResponse responseMissingDetail = Mockito.mock(ClientHttpResponse.class);
        Mockito.when(responseMissingDetail.getBody())
                .thenReturn(new ByteArrayInputStream(errorJsonMissingDetail.getBytes(StandardCharsets.UTF_8)));
        Mockito.when(responseMissingDetail.getStatusCode()).thenReturn(HttpStatus.SERVICE_UNAVAILABLE);

        VerifierException ex2 = assertThrows(VerifierException.class, () -> errorHandler.handle(request, responseMissingDetail));
        assertEquals(SERVICE_UNAVAILABLE, ex2.getStatus());
        assertEquals("Description: null Detail: This is an error", ex2.getMessage());
        assertFalse(ex2.getIsBusinessError());

        // Error JSON missing description
        String errorJsonMissingDescription = "{\"detail\":\"This is a detail\"}";
        ClientHttpResponse responseMissingDescription = Mockito.mock(ClientHttpResponse.class);
        Mockito.when(responseMissingDescription.getBody())
                .thenReturn(new ByteArrayInputStream(errorJsonMissingDescription.getBytes(StandardCharsets.UTF_8)));
        Mockito.when(responseMissingDescription.getStatusCode()).thenReturn(HttpStatus.SERVICE_UNAVAILABLE);

        VerifierException ex3 = assertThrows(VerifierException.class, () -> errorHandler.handle(request, responseMissingDescription));
        assertEquals(SERVICE_UNAVAILABLE, ex3.getStatus());
        assertEquals("Description: This is a detail Detail: null", ex3.getMessage());
        assertFalse(ex3.getIsBusinessError());
    }

    @Test
    void handle_businessError_throwsVerifierExceptionWithBusinessFlag() throws IOException {
        String errorJson = "{\"detail\":\"The verification with the identifier '34234' was not found\"}";
        ClientHttpResponse response = Mockito.mock(ClientHttpResponse.class);
        Mockito.when(response.getBody()).thenReturn(new ByteArrayInputStream(errorJson.getBytes(StandardCharsets.UTF_8)));
        Mockito.when(response.getStatusCode()).thenReturn(HttpStatus.NOT_FOUND);
        HttpRequest request = Mockito.mock(HttpRequest.class);

        VerifierException ex = assertThrows(VerifierException.class, () -> errorHandler.handle(request, response));

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatus());
        assertEquals("The verification with the identifier '34234' was not found", ex.getMessage());
        assertTrue(ex.getIsBusinessError());
    }






}
