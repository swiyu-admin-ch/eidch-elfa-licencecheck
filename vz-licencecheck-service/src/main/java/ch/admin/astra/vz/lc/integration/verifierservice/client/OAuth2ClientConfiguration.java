package ch.admin.astra.vz.lc.integration.verifierservice.client;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.*;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;

/**
 * Configuration for OAuth2 client credentials flow used for service-to-service communication.
 * This configuration is only active when oauth2 authentication is enabled.
 */
@Slf4j
@Configuration
@ConditionalOnProperty(name = "verifier-service.oauth2.enable", havingValue = "true")
@ConditionalOnClass(name = "org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager")
public class OAuth2ClientConfiguration {

    /**
     * Creates an OAuth2AuthorizedClientManager for client credentials flow.
     * This manager handles token acquisition and refresh automatically.
     */
    @Bean
    public OAuth2AuthorizedClientManager authorizedClientManager(
            ClientRegistrationRepository clientRegistrationRepository,
            OAuth2AuthorizedClientService authorizedClientService) {

        OAuth2AuthorizedClientProvider authorizedClientProvider =
                OAuth2AuthorizedClientProviderBuilder.builder()
                        .clientCredentials()
                        .build();

        AuthorizedClientServiceOAuth2AuthorizedClientManager authorizedClientManager =
                new AuthorizedClientServiceOAuth2AuthorizedClientManager(
                        clientRegistrationRepository,
                        authorizedClientService);

        authorizedClientManager.setAuthorizedClientProvider(authorizedClientProvider);

        log.info("OAuth2 client credentials manager initialized");

        return authorizedClientManager;
    }
}

