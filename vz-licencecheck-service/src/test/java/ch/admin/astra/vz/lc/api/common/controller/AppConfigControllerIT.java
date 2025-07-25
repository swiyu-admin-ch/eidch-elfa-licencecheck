package ch.admin.astra.vz.lc.api.common.controller;

import ch.admin.astra.vz.lc.BaseIntegrationTest;
import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AppConfigControllerIT extends BaseIntegrationTest {

    private static final String APP_CONFIG_BASE_URL = "/api/app-config";


    @Test
    void testGetAppConfig() throws Exception {
        mvc.perform(get(APP_CONFIG_BASE_URL)).andExpect(status().isOk());
    }
}
