package ch.admin.astra.vz.lc.integration.verifiermanagement.client;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
@TestPropertySource(locations={"classpath:application.yml", "classpath:application-ref.yml"})
@ActiveProfiles(value = "ref")
@SpringBootTest(properties="spring.main.lazy-initialization=true")
class VerifierAgentManagementClientConfigurationImplIT {

    @Autowired
    private VerifierAgentManagementClient verifierAgentManagementClient;

    @Test
    void assertTechAdapterClient() {
        assertInstanceOf(VerifierAgentManagementImpl.class, verifierAgentManagementClient);
    }
}