package ch.admin.astra.vz.lc.api.common.controller;

import ch.admin.astra.vz.lc.junit.VZIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@VZIntegrationTest
class AppConfigControllerIT {

    private static final String APP_CONFIG_BASE_URL = "/api/app-config";

    @Autowired
    private MockMvc mvc;

    @Test
    void testGetAppConfig() throws Exception {
        mvc.perform(get(APP_CONFIG_BASE_URL)).andExpect(status().isOk());
    }
}
