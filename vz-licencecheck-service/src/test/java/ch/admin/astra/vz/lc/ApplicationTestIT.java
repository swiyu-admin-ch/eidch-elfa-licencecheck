package ch.admin.astra.vz.lc;

import ch.admin.astra.vz.lc.junit.VZIntegrationTest;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@VZIntegrationTest
class ApplicationTestIT {

    @Test
    void contextLoads() {
        final var contextLoads = true;
        assertThat(contextLoads).isTrue();
    }
}
