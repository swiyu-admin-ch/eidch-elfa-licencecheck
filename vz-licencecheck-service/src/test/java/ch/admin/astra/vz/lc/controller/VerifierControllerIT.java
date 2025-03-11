package ch.admin.astra.vz.lc.controller;

import ch.admin.astra.vz.lc.BaseIntegrationTest;
import ch.admin.astra.vz.lc.api.*;
import ch.admin.astra.vz.lc.domain.qrcode.QrCodeService;
import ch.admin.astra.vz.lc.domain.vam.configuration.interceptor.VAMLoggingInterceptor;
import ch.admin.astra.vz.lc.domain.vam.model.ManagementResponseDto;
import ch.admin.astra.vz.lc.domain.vam.model.ResponseDataDto;
import ch.admin.astra.vz.lc.domain.vam.model.VerificationState;
import ch.admin.astra.vz.lc.domain.vam.service.VerifierAgentManagementClient;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static ch.admin.astra.vz.lc.api.VerificationStatus.SUCCESS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.doReturn;

class VerifierControllerIT extends BaseIntegrationTest {

    public static final UUID ID = UUID.fromString("11111111-2222-3333-4444-555555555555");
    private static final String EXISTING_USE_CASE = "c2041c31-db6b-4cf1-871d-6a24d400159b";

    @MockBean
    private VerifierAgentManagementClient verifierAgentManagementClient;

    @MockBean
    private VAMLoggingInterceptor vamLoggingInterceptor;

    @Autowired
    private QrCodeService qrCodeService;

    @Test
    void testGetUseCases_thenSuccess() throws Exception {

        List<UseCaseResponse> response = fetchGet("use-cases", new TypeReference<>() {
        });

        assertNotNull(response);
        Assertions.assertEquals(1, response.size());

        UseCaseResponse useCaseResponse = response.getFirst();
        assertEquals("title", useCaseResponse.getTitle());
        assertEquals("description", useCaseResponse.getDescription());

        List<AttributeGroupResponse> attributeGroups = useCaseResponse.getAttributeGroups();
        assertEquals(1, attributeGroups.size());

        AttributeGroupResponse attributeGroupResponse = attributeGroups.getFirst();
        assertEquals("name", attributeGroupResponse.getName());
        assertEquals(1, attributeGroupResponse.getOrder());

        List<AttributeResponse> attributes = attributeGroupResponse.getAttributes();
        AttributeResponse attributeResponse = attributes.getFirst();
        assertEquals("name", attributeResponse.getName());
        assertEquals("type", attributeResponse.getType());
        assertEquals(1, attributeResponse.getOrder());
    }

    @Test
    void testStartVerificationProcess_usingExistingUseCase_thenSuccess() throws Exception {
        doReturn(getTestVerificationResponse()).when(verifierAgentManagementClient)
            .createVerification(any());

        StartVerificationRequest request = StartVerificationRequest.builder().useCaseId(UUID.fromString(EXISTING_USE_CASE)).build();
        VerificationBeginResponse response = fetchPost("verify", request, new TypeReference<>() {
        });

        assertNotNull(response);
        assertEquals(ID, response.getId());
        assertEquals(Base64.getEncoder().encodeToString(qrCodeService.create("url", 500).getImageData()), Base64.getEncoder().encodeToString(response.getQrCode()));
        assertEquals("png", response.getQrCodeFormat());
    }

    @Test
    void testGetVerificationProcess_usingMockedProcess_thenSuccess() throws Exception {
        doReturn(getTestVerificationResponse()).when(verifierAgentManagementClient)
            .getVerificationStatus(argThat(r -> r.equals(ID)));


        VerificationStatusResponse response = fetchGet("verify/" + ID, new TypeReference<>() {
        });

        assertNotNull(response);

        assertEquals(ID.toString(), response.getId());
        assertEquals(SUCCESS, response.getStatus());
        assertEquals("url", response.getVerificationUrl());

        assertThat(response.getHolderResponse().getAttributes()).containsExactlyEntriesOf(
            Map.of("attributeKey", "attributeValue"));
    }

    private ManagementResponseDto getTestVerificationResponse() {
        return ManagementResponseDto.builder()
            .id(ID)
            .state(VerificationState.SUCCESS)
            .verificationUrl("url")
            .walletResponse(ResponseDataDto.builder()
                .credentialSubjectData(Map.of(
                    "attributeKey", "attributeValue"
                ))
                .build())
            .build();
    }
}