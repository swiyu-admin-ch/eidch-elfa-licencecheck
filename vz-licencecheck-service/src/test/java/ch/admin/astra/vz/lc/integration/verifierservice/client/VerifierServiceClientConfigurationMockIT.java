package ch.admin.astra.vz.lc.integration.verifierservice.client;

import ch.admin.astra.vz.lc.junit.VZIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

@VZIntegrationTest
class VerifierServiceClientConfigurationMockIT {

    @Autowired
    private VerifierServiceClient verifierServiceClient;

    @Test
    void assertTechAdapterClient() {
        assertThat(verifierServiceClient).isInstanceOf(MockVerifierServiceImpl.class);
    }
}
