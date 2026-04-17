package ch.admin.astra.vz.lc.integration.verifierservice.client;

import ch.admin.astra.vz.controller.verifier.model.VerificationErrorResponseCodeDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Scope;

/**
 * Configuration for Mock Verifier Service Client.
 * Only active for local profile for development and testing.
 */
@Configuration
@RequiredArgsConstructor
@Profile({"local", "test"})
public class MockVerifierServiceClientConfiguration {

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
    public VerifierServiceClient verifierServiceClient(
        @Value("${verifier-service.mock.failResponse}") Boolean failResponse,
        @Value("${verifier-service.mock.errorCode}") VerificationErrorResponseCodeDto errorCode) {
        return new MockVerifierServiceImpl(failResponse, errorCode);
    }
}
