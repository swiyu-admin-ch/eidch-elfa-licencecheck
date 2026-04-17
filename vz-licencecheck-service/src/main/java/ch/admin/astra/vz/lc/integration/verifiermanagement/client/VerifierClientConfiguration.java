package ch.admin.astra.vz.lc.integration.verifiermanagement.client;

import ch.admin.astra.vz.lc.core.features.FeaturesProperties;
import ch.admin.astra.vz.lc.integration.verifiermanagement.client.interceptor.HttpLogRequestInterceptor;
import ch.admin.astra.vz.lc.integration.verifiermanagement.client.interceptor.VerifierHeaderInterceptor;
import ch.admin.astra.vz.lc.integration.verifiermanagement.client.model.VerificationErrorResponseCodeDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.RestClient;

@Configuration
@RequiredArgsConstructor
public class VerifierClientConfiguration {

    private final ObjectMapper objectMapper;
    private final FeaturesProperties features;
    private final VerifierHeaderInterceptor verifierHeaderInterceptor;

    @Profile("!local")
    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
    public VerifierApi verifierApi(RestClient.Builder builder,
                                   @Value("${verifier-service.endpoint}") String endpoint) {
        RestClient client = restClient(builder, endpoint);
        return createVerifierApi(client);
    }

    @Profile("!local")
    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
    public VerifierServiceClient verifierManagementImpl(RestClient.Builder builder,
                                                        @Value("${verifier-service.endpoint}") String endpoint) {
        VerifierApi api = verifierApi(builder, endpoint);
        return new VerifierServiceImpl(api);
    }

    @Profile("local")
    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
    public VerifierServiceClient mockVerifierManagementImpl(
            @Value("${verifier.mock.failResponse}") Boolean failResponse,
            @Value("${verifier.mock.errorCode}") VerificationErrorResponseCodeDto errorCode) {
        return new MockVerifierServiceImpl(failResponse, errorCode);
    }

    VerifierApi createVerifierApi(RestClient client) {
        if (features.isUseSingleVerifierEnabled()) {
            return new VerifierServiceApi(client);
        } else {
            return new VerifierManagementApi(client);
        }
    }

    @Bean
    public HttpLogRequestInterceptor httpLogRequestInterceptor() {
        return new HttpLogRequestInterceptor();
    }

    public RestClient restClient(RestClient.Builder builder, String endpoint) {
        return builder
                .baseUrl(endpoint)
                .defaultStatusHandler(
                        HttpStatusCode::isError, // 4xx oder 5xx
                        new RestErrorHandler(objectMapper))
                .requestInterceptor(verifierHeaderInterceptor)
                .requestInterceptor(httpLogRequestInterceptor())
                .build();
    }

}