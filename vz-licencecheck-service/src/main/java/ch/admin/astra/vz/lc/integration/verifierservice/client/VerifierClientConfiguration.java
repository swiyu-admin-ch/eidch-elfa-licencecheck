package ch.admin.astra.vz.lc.integration.verifierservice.client;

import ch.admin.astra.vz.lc.integration.verifierservice.client.interceptor.HttpLogRequestInterceptor;
import ch.admin.astra.vz.lc.integration.verifierservice.client.interceptor.OAuth2ClientCredentialsInterceptor;
import ch.admin.astra.vz.lc.integration.verifierservice.client.interceptor.VerifierHeaderInterceptor;
import ch.admin.astra.vz.lc.integration.verifierservice.client.model.VerificationErrorResponseCodeDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.web.client.RestClient;

import java.util.Optional;

@Slf4j
@Configuration
public class VerifierClientConfiguration {

    private final ObjectMapper objectMapper;
    private final VerifierHeaderInterceptor verifierHeaderInterceptor;
    private final Optional<OAuth2AuthorizedClientManager> oAuth2AuthorizedClientManager;

    public VerifierClientConfiguration(ObjectMapper objectMapper,
                                       VerifierHeaderInterceptor verifierHeaderInterceptor,
                                       @Autowired(required = false) OAuth2AuthorizedClientManager oAuth2AuthorizedClientManager) {
        this.objectMapper = objectMapper;
        this.verifierHeaderInterceptor = verifierHeaderInterceptor;
        this.oAuth2AuthorizedClientManager = Optional.ofNullable(oAuth2AuthorizedClientManager);
    }

    @Profile("!local")
    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
    public VerifierServiceClient verifierManagementImpl(RestClient.Builder builder,
                                                        @Value("${verifier-service.endpoint}") String endpoint,
                                                        @Value("${verifier-service.oauth2.enable:false}") boolean oauth2Enabled,
                                                        @Value("${verifier-service.oauth2.client-registration-id:licencecheck}") String oauth2ClientRegistrationId) {
        RestClient client = restClient(builder, endpoint, oauth2Enabled, oauth2ClientRegistrationId);

        VerifierApi api = new VerifierServiceApi(client);
        return new VerifierServiceImpl(api);
    }

    @Profile("local")
    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
    public VerifierServiceClient mockVerifierManagementImpl(
            @Value("${verifier-service.mock.failResponse}") Boolean failResponse,
            @Value("${verifier-service.mock.errorCode}") VerificationErrorResponseCodeDto errorCode) {
        return new MockVerifierServiceImpl(failResponse, errorCode);
    }

    @Bean
    public HttpLogRequestInterceptor httpLogRequestInterceptor() {
        return new HttpLogRequestInterceptor();
    }

    public RestClient restClient(RestClient.Builder builder, String endpoint, boolean oauth2Enabled, String oauth2ClientRegistrationId) {
        builder.baseUrl(endpoint)
                .defaultStatusHandler(
                        HttpStatusCode::isError, // 4xx oder 5xx
                        new RestErrorHandler(objectMapper))
                .requestInterceptor(verifierHeaderInterceptor)
                .requestInterceptor(httpLogRequestInterceptor());

        if (oauth2Enabled && oAuth2AuthorizedClientManager.isPresent()) {
            OAuth2ClientCredentialsInterceptor oauth2Interceptor =
                    new OAuth2ClientCredentialsInterceptor(
                            oAuth2AuthorizedClientManager.get(),
                            oauth2ClientRegistrationId);
            builder.requestInterceptor(oauth2Interceptor);
            log.info("OAuth2 interceptor enabled for client registration: {}", oauth2ClientRegistrationId);
        }

        return builder.build();
    }

}