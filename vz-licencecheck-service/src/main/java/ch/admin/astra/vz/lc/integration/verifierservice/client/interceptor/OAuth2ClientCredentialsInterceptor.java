package ch.admin.astra.vz.lc.integration.verifierservice.client.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;

import java.io.IOException;

/**
 * Interceptor that adds OAuth2 Bearer token to outgoing HTTP requests.
 * Uses Spring Security's OAuth2 client credentials flow to automatically
 * obtain and refresh access tokens.
 */
@Slf4j
public class OAuth2ClientCredentialsInterceptor implements ClientHttpRequestInterceptor {

    private final OAuth2AuthorizedClientManager authorizedClientManager;
    private final String clientRegistrationId;

    public OAuth2ClientCredentialsInterceptor(
            OAuth2AuthorizedClientManager authorizedClientManager,
            String clientRegistrationId) {
        this.authorizedClientManager = authorizedClientManager;
        this.clientRegistrationId = clientRegistrationId;
    }

    @Override
    @NotNull
    public ClientHttpResponse intercept(@NotNull HttpRequest request, @NotNull byte[] body, @NotNull ClientHttpRequestExecution execution) throws IOException {
        OAuth2AuthorizedClient authorizedClient = authorize();

        if (authorizedClient != null && authorizedClient.getAccessToken() != null) {
            String tokenValue = authorizedClient.getAccessToken().getTokenValue();
            request.getHeaders().set(HttpHeaders.AUTHORIZATION, "Bearer " + tokenValue);
            log.debug("OAuth2ClientCredentialsInterceptor: Added Bearer token to request");
        } else {
            log.warn("OAuth2ClientCredentialsInterceptor: No access token available for client registration: {}", clientRegistrationId);
        }

        return execution.execute(request, body);
    }

    private OAuth2AuthorizedClient authorize() {
        OAuth2AuthorizeRequest authorizeRequest = OAuth2AuthorizeRequest
                .withClientRegistrationId(clientRegistrationId)
                .principal(clientRegistrationId) // Use client registration ID as principal for service-to-service auth
                .build();

        return authorizedClientManager.authorize(authorizeRequest);
    }
}

