package ch.admin.astra.vz.lc.integration.verifierservice.client;

import ch.admin.astra.vz.lc.integration.verifierservice.client.interceptor.HttpLogRequestInterceptor;
import ch.admin.astra.vz.lc.integration.verifierservice.client.interceptor.OAuth2ClientCredentialsInterceptor;
import ch.admin.astra.vz.lc.integration.verifierservice.client.interceptor.VerifierHeaderInterceptor;
import ch.admin.astra.vz.lc.integration.verifierservice.client.mapper.VerifierServiceModelMapper;
import ch.admin.astra.vz.controller.verifier.api.VerifierManagementApiApi;
import ch.admin.astra.vz.controller.verifier.invoker.ApiClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.web.client.RestClient;

import java.util.Optional;

/**
 * Configuration class for the OpenAPI-based VerifierServiceClient implementation.
 * This configuration is activated when the feature flag generatedVerifierClient is enabled (default).
 */
@Slf4j
@Configuration
@Profile("!local")
@ConditionalOnProperty(
        name = "features.GENERATED_VERIFIER_CLIENT",
        havingValue = "true",
        matchIfMissing = true
)
public class OpenAPIVerifierClientConfiguration {

    private final VerifierServiceModelMapper mapper;
    private final VerifierHeaderInterceptor verifierHeaderInterceptor;
    private final Optional<OAuth2AuthorizedClientManager> oAuth2AuthorizedClientManager;

    public OpenAPIVerifierClientConfiguration(
            VerifierServiceModelMapper mapper,
            VerifierHeaderInterceptor verifierHeaderInterceptor,
            @Autowired(required = false) OAuth2AuthorizedClientManager oAuth2AuthorizedClientManager) {
        this.mapper = mapper;
        this.verifierHeaderInterceptor = verifierHeaderInterceptor;
        this.oAuth2AuthorizedClientManager = Optional.ofNullable(oAuth2AuthorizedClientManager);
    }

    @Bean
    @NotNull
    public RestErrorHandler restErrorHandler(ObjectMapper objectMapper) {
        return new RestErrorHandler(objectMapper);
    }

    @Bean
    public HttpLogRequestInterceptor httpLogRequestInterceptor() {
        return new HttpLogRequestInterceptor();
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
    public VerifierServiceClient verifierServiceClient(
            @Value("${verifier-service.endpoint}") String endpoint,
            @Value("${verifier-service.oauth2.enable:false}") boolean oauth2Enabled,
            @Value("${verifier-service.oauth2.client-registration-id:licencecheck}") String oauth2ClientRegistrationId,
            RestErrorHandler restErrorHandler,
            HttpLogRequestInterceptor httpLogRequestInterceptor,
            ObjectMapper objectMapper) {


        RestClient.Builder restClientBuilder = RestClient.builder();

        // Buffered request factory to allow multiple reads of the response body (for logging)
        var httpFactory = new BufferingClientHttpRequestFactory(
                new HttpComponentsClientHttpRequestFactory()
        );

        // Configure RestClient with all necessary interceptors and error handling
        restClientBuilder
                .requestFactory(httpFactory)
                .defaultStatusHandler(
                        HttpStatusCode::isError, // 4xx oder 5xx
                        restErrorHandler)
                .requestInterceptor(verifierHeaderInterceptor)
                .requestInterceptor(httpLogRequestInterceptor);

        // Add OAuth2 interceptor if enabled
        if (oauth2Enabled && oAuth2AuthorizedClientManager.isPresent()) {
            OAuth2ClientCredentialsInterceptor oauth2Interceptor =
                    new OAuth2ClientCredentialsInterceptor(
                            oAuth2AuthorizedClientManager.get(),
                            oauth2ClientRegistrationId);
            restClientBuilder.requestInterceptor(oauth2Interceptor);
            log.info("OAuth2 interceptor enabled for OpenAPI client with registration: {}", oauth2ClientRegistrationId);
        }

        RestClient restClient = restClientBuilder.build();

        // Configure ObjectMapper to exclude null and empty values from serialization
        // This prevents sending null fields and empty collections/maps in requests to the Verifier Service
        ObjectMapper configuredMapper = objectMapper.copy();
        configuredMapper.setSerializationInclusion(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY);

        // Create ApiClient and configure it to use our RestClient and ObjectMapper
        // This ensures that the ObjectMapper with NON_EMPTY configuration is used
        ApiClient apiClient = new ApiClient(restClient, configuredMapper, ApiClient.createDefaultDateFormat());

        // Override Verifier URL - needs to happen here because the generated ApiClient
        // uses a hardcoded default base path which we need to override
        apiClient.setBasePath(endpoint);

        VerifierManagementApiApi api = new VerifierManagementApiApi(apiClient);

        return new OpenAPIVerifierServiceImpl(api, mapper);
    }
}

