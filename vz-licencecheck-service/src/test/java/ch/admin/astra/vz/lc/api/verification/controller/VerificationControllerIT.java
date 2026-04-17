package ch.admin.astra.vz.lc.api.verification.controller;

import ch.admin.astra.vz.controller.verifier.model.ManagementResponseDto;
import ch.admin.astra.vz.controller.verifier.model.ResponseDataDto;
import ch.admin.astra.vz.controller.verifier.model.VerificationStatusDto;
import ch.admin.astra.vz.lc.api.verification.model.StartVerificationDto;
import ch.admin.astra.vz.lc.api.verification.model.UseCaseDto;
import ch.admin.astra.vz.lc.api.verification.model.VerificationBeginResponseDto;
import ch.admin.astra.vz.lc.api.verification.model.VerificationStateDto;
import ch.admin.astra.vz.lc.integration.verifierservice.client.VerifierServiceClient;
import ch.admin.astra.vz.lc.integration.verifierservice.client.interceptor.HttpLogRequestInterceptor;
import ch.admin.astra.vz.lc.junit.VZIntegrationTest;
import ch.admin.astra.vz.lc.modules.verification.service.qrcode.QrCodeService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static ch.admin.astra.vz.lc.api.verification.model.StatusDto.SUCCESS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration test for VerificationController 'old' VerifierServiceClient not OpenApi Client.
 */
@VZIntegrationTest
class VerificationControllerIT {

    private static final String BASE_URL = "/api/v1/verification/";
    private static final UUID ID = UUID.fromString("11111111-2222-3333-4444-555555555555");
    private static final String EXISTING_USE_CASE = "c2041c31-db6b-4cf1-871d-6a24d400159b";

    @MockitoBean
    private VerifierServiceClient verifierServiceClient;

    @MockitoBean
    private HttpLogRequestInterceptor verifierLoggingInterceptor;

    @Autowired
    private QrCodeService qrCodeService;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetUseCases_thenSuccess() throws Exception {
        // when
        final var mvcResult = mvc.perform(get(BASE_URL + "use-cases"))
            .andExpect(status().isOk())
            .andReturn();

        // then
        final var response = getResponse(mvcResult, new TypeReference<List<UseCaseDto>>() {});
        assertThat(response).isNotNull().hasSize(1);

        final var useCaseDto = response.getFirst();
        assertThat(useCaseDto.title()).isEqualTo("title");
        assertThat(useCaseDto.description()).isEqualTo("description");

        final var attributeGroups = useCaseDto.attributeGroups();
        assertThat(attributeGroups).hasSize(1);

        final var attributeGroupDto = attributeGroups.getFirst();
        assertThat(attributeGroupDto.name()).isEqualTo("name");
        assertThat(attributeGroupDto.order()).isEqualTo(1);

        final var attributeDto = attributeGroupDto.attributes().getFirst();
        assertThat(attributeDto.name()).isEqualTo("name");
        assertThat(attributeDto.type()).isEqualTo("type");
        assertThat(attributeDto.order()).isEqualTo(1);
    }

    @Test
    void testStartVerificationProcess_usingExistingUseCase_thenSuccess() throws Exception {
        // given
        final var requestContent = new StartVerificationDto(UUID.fromString(EXISTING_USE_CASE));
        final var expectedQrCode = qrCodeService.create("swiyu://blahblah", 500).getImageData();
        given(verifierServiceClient.createVerification(any())).willReturn(getTestVerificationResponse());

        // when
        final var mvcResult = mvc.perform(post(BASE_URL + "verify")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestContent)))
            .andExpect(status().isOk())
            .andReturn();

        // then
        final var response = getResponse(mvcResult, VerificationBeginResponseDto.class);
        assertThat(response).isNotNull();

        assertThat(response.id()).isEqualTo(ID);
        assertThat(Base64.getEncoder().encodeToString(response.qrCode()))
            .isEqualTo(Base64.getEncoder().encodeToString(expectedQrCode));
        assertThat(response.qrCodeFormat()).isEqualTo("png");
    }

    @Test
    void testGetVerificationProcess_usingMockedProcess_thenSuccess() throws Exception {
        // given
        given(verifierServiceClient.getVerificationStatus(argThat(uuid -> uuid.equals(ID))))
            .willReturn(getTestVerificationResponse());

        // when
        final var mvcResult = mvc.perform(get(BASE_URL + "verify/{id}", ID))
            .andExpect(status().isOk())
            .andReturn();

        // then
        final var response = getResponse(mvcResult, VerificationStateDto.class);
        assertThat(response).isNotNull();

        assertThat(response.id()).isEqualTo(ID.toString());
        assertThat(response.status()).isEqualTo(SUCCESS);
        assertThat(response.verificationUrl()).isEqualTo("url");
        assertThat(response.holderAttributes().attributes())
            .containsExactlyEntriesOf(Map.of("attributeKey", "attributeValue"));
    }

    // Helper Methods

    private <T> T getResponse(MvcResult result, TypeReference<T> valueType) throws Exception {
        final var responseContent = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
        return objectMapper.readValue(responseContent, valueType);
    }

    private <T> T getResponse(MvcResult result, Class<T> valueType) throws Exception {
        final var responseContent = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
        return objectMapper.readValue(responseContent, valueType);
    }

    private ManagementResponseDto getTestVerificationResponse() {
        return ManagementResponseDto.builder()
            .id(ID)
            .state(VerificationStatusDto.SUCCESS)
            .verificationUrl("url")
            .verificationDeeplink("swiyu://blahblah")
            .walletResponse(ResponseDataDto.builder()
                .credentialSubjectData(Map.of(
                    "attributeKey", "attributeValue"
                ))
                .build())
            .build();
    }
}
