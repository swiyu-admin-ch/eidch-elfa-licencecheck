package ch.admin.astra.vz.lc.api.verification.controller;

import ch.admin.astra.vz.lc.BaseIntegrationTest;
import ch.admin.astra.vz.lc.api.verification.model.*;
import ch.admin.astra.vz.lc.integration.verifiermanagement.client.VerifierAgentManagementClient;
import ch.admin.astra.vz.lc.integration.verifiermanagement.client.interceptor.VAMLoggingInterceptor;
import ch.admin.astra.vz.lc.integration.verifiermanagement.client.model.ManagementResponseDto;
import ch.admin.astra.vz.lc.integration.verifiermanagement.client.model.ResponseDataDto;
import ch.admin.astra.vz.lc.integration.verifiermanagement.client.model.VerificationStatusDto;
import ch.admin.astra.vz.lc.modules.verification.service.qrcode.QrCodeService;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static ch.admin.astra.vz.lc.api.verification.model.StatusDto.SUCCESS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.doReturn;


class VerificationControllerIT extends BaseIntegrationTest {

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

        List<UseCaseDto> response = fetchGet("use-cases", new TypeReference<>() {
        });

        assertNotNull(response);
        Assertions.assertEquals(1, response.size());

        UseCaseDto useCaseDto = response.getFirst();
        assertEquals("title", useCaseDto.title());
        assertEquals("description", useCaseDto.description());

        List<AttributeGroupDto> attributeGroups = useCaseDto.attributeGroups();
        assertEquals(1, attributeGroups.size());

        AttributeGroupDto attributeGroupDto = attributeGroups.getFirst();
        assertEquals("name", attributeGroupDto.name());
        assertEquals(1, attributeGroupDto.order());

        List<AttributeDto> attributes = attributeGroupDto.attributes();
        AttributeDto attributeDto = attributes.getFirst();
        assertEquals("name", attributeDto.name());
        assertEquals("type", attributeDto.type());
        assertEquals(1, attributeDto.order());
    }

    @Test
    void testStartVerificationProcess_usingExistingUseCase_thenSuccess() throws Exception {
        doReturn(getTestVerificationResponse()).when(verifierAgentManagementClient)
            .createVerification(any());

        StartVerificationDto request = new StartVerificationDto(UUID.fromString(EXISTING_USE_CASE));
        VerificationBeginResponseDto response = fetchPost("verify", request, new TypeReference<>() {
        });

        assertNotNull(response);
        assertEquals(ID, response.id());
        assertEquals(Base64.getEncoder().encodeToString(qrCodeService.create("url", 500).getImageData()), Base64.getEncoder().encodeToString(response.qrCode()));
        assertEquals("png", response.qrCodeFormat());
    }

    @Test
    void testGetVerificationProcess_usingMockedProcess_thenSuccess() throws Exception {
        doReturn(getTestVerificationResponse()).when(verifierAgentManagementClient)
            .getVerificationStatus(argThat(r -> r.equals(ID)));


        VerificationStateDto response = fetchGet("verify/" + ID, new TypeReference<>() {
        });

        assertNotNull(response);

        assertEquals(ID.toString(), response.id());
        assertEquals(SUCCESS, response.status());
        assertEquals("url", response.verificationUrl());

        assertThat(response.holderAttributes().attributes()).containsExactlyEntriesOf(
            Map.of("attributeKey", "attributeValue"));
    }

    private ManagementResponseDto getTestVerificationResponse() {
        return ManagementResponseDto.builder()
            .id(ID)
                .state(VerificationStatusDto.SUCCESS)
            .verificationUrl("url")
            .walletResponse(ResponseDataDto.builder()
                .credentialSubjectData(Map.of(
                    "attributeKey", "attributeValue"
                ))
                .build())
            .build();
    }
}