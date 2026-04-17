package ch.admin.astra.vz.lc.integration.verifierservice.client;

import ch.admin.astra.vz.commons.error.rest.ExternalServiceRestErrorHandler;
import ch.admin.astra.vz.lc.integration.verifierservice.client.interceptor.HttpLogRequestInterceptor;
import ch.admin.astra.vz.lc.integration.verifierservice.client.interceptor.VerifierHeaderInterceptor;
import ch.admin.astra.vz.controller.verifier.api.VerifierManagementApiApi;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit test for OpenAPIVerifierClientConfiguration to verify that different configured URLs
 * are actually used when creating the VerifierServiceClient bean.
 */
@ExtendWith(MockitoExtension.class)
class OpenAPIVerifierClientConfigurationTest {

    @Mock
    private VerifierHeaderInterceptor verifierHeaderInterceptor;

    private OpenAPIVerifierClientConfiguration configuration;
    private ObjectMapper objectMapper;
    private ExternalServiceRestErrorHandler restErrorHandler;
    private HttpLogRequestInterceptor httpLogRequestInterceptor;

    @BeforeEach
    void setUp() {
        configuration = new OpenAPIVerifierClientConfiguration(verifierHeaderInterceptor, null);
        objectMapper = new ObjectMapper();
        restErrorHandler = configuration.restErrorHandler(objectMapper);
        httpLogRequestInterceptor = new HttpLogRequestInterceptor();
    }

    @Test
    void verifierServiceClient_shouldUseConfiguredEndpoint() {
        // Given
        String expectedEndpoint = "https://verifier-default.example.com";

        // When
        VerifierServiceClient client = configuration.verifierServiceClient(
                expectedEndpoint,
                false,
                "licencecheck",
                restErrorHandler,
                httpLogRequestInterceptor,
                objectMapper
        );

        // Then
        assertThat(client)
                .isNotNull()
                .isInstanceOf(OpenAPIVerifierServiceImpl.class);

        VerifierManagementApiApi api = extractApiFromClient(client);
        assertThat(api.getApiClient().getBasePath())
                .as("The ApiClient should use the configured endpoint as base path")
                .isEqualTo(expectedEndpoint);
    }


    private VerifierManagementApiApi extractApiFromClient(VerifierServiceClient client) {
        OpenAPIVerifierServiceImpl impl = (OpenAPIVerifierServiceImpl) client;
        VerifierManagementApiApi api = (VerifierManagementApiApi) ReflectionTestUtils.getField(impl, "verifierManagementApiApi");
        assertThat(api)
                .as("API should be set in the implementation")
                .isNotNull();
        return api;
    }
}