package ch.admin.astra.vz.lc.domain.verifier;

import ch.admin.astra.vz.lc.domain.qrcode.QrCodeService;
import ch.admin.astra.vz.lc.domain.qrcode.exception.ImageHandlingException;
import ch.admin.astra.vz.lc.domain.qrcode.model.QrCode;
import ch.admin.astra.vz.lc.domain.vam.exception.VAMException;
import ch.admin.astra.vz.lc.domain.vam.model.CreateVerificationManagementDto;
import ch.admin.astra.vz.lc.domain.vam.model.ManagementResponseDto;
import ch.admin.astra.vz.lc.domain.vam.model.VerificationStatusDto;
import ch.admin.astra.vz.lc.domain.vam.service.VerifierAgentManagementClient;
import ch.admin.astra.vz.lc.domain.verifier.exception.UseCaseNotFoundException;
import ch.admin.astra.vz.lc.domain.verifier.model.Attribute;
import ch.admin.astra.vz.lc.domain.verifier.model.AttributeGroup;
import ch.admin.astra.vz.lc.domain.verifier.model.UseCase;
import ch.admin.astra.vz.lc.domain.verifier.model.VerificationBeginResponseDto;
import ch.admin.astra.vz.lc.logging.LoggingService;
import ch.admin.astra.vz.lc.mapper.UseCaseMapper;
import ch.admin.astra.vz.lc.mapper.VerificationMapper;
import ch.qos.logback.classic.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VerificationServiceTest {


    public static final String CLIENT_NAME = "Client";
    public static final String ALLOWED_ISSUER_DID = "did:example:123";
    private VerificationService verificationService;

    @Mock
    private UseCaseCache useCaseCache;

    @Mock
    private UseCaseMapper useCaseMapper;

    @Mock
    private VerifierAgentManagementClient verifierAgentManagementClient;

    @Mock
    private VerificationMapper verificationMapper;

    @Mock
    private QrCodeService qrCodeService;

    @Spy
    private LoggingService loggingService;

    @BeforeEach
    void setUp() {
        verificationService = new VerificationService(useCaseCache, useCaseMapper, verifierAgentManagementClient, verificationMapper, qrCodeService, loggingService, CLIENT_NAME, ALLOWED_ISSUER_DID);
    }

    @AfterEach
    void teardown() {
        ((Logger) LoggerFactory.getLogger(VerificationService.class)).detachAndStopAllAppenders();
    }

    @Test
    void testGetUseCases_callsCache() {
        verificationService.getUseCases();
        Mockito.verify(useCaseCache).getUseCases();
    }

    @Test
    void createVerification_UseCaseNotFoundException() {
        // given
        UUID useCaseId = UUID.randomUUID();
        when(useCaseCache.getUseCaseById(useCaseId)).thenThrow(UseCaseNotFoundException.class);

        // when
        Assertions.assertThrows(UseCaseNotFoundException.class, () -> verificationService.createVerification(useCaseId));

        // then
        Mockito.verify(useCaseCache).getUseCaseById(useCaseId);
    }

    @Test
    void createVerification_TechAdapterException() {
        // given
        // mock useCaseCache
        UUID useCaseId = UUID.randomUUID();
        when(useCaseCache.getUseCaseById(useCaseId)).thenReturn(mockUseCase());

        // mock verifierAgentManagementClient
        when(verifierAgentManagementClient.createVerification(any())).thenThrow(VAMException.class);

        // when
        Assertions.assertThrows(VAMException.class, () -> verificationService.createVerification(useCaseId));

        // then
        // assert useCaseService
        Mockito.verify(useCaseCache).getUseCaseById(useCaseId);

        // assert CreateVerificationManagementDto
        ArgumentCaptor<CreateVerificationManagementDto> createVerificationManagementDtoArgumentCaptor = ArgumentCaptor.forClass(CreateVerificationManagementDto.class);
        Mockito.verify(verifierAgentManagementClient).createVerification(createVerificationManagementDtoArgumentCaptor.capture());
        CreateVerificationManagementDto createVerificationManagementDto = createVerificationManagementDtoArgumentCaptor.getValue();
        assertIterableEquals(List.of("$.attribute2"), createVerificationManagementDto.presentationDefinition().getInputDescriptors().getFirst().constraints().fields().getFirst().path());
        assertIterableEquals(List.of("$.attribute"), createVerificationManagementDto.presentationDefinition().getInputDescriptors().getFirst().constraints().fields().get(1).path());
        assertEquals(CLIENT_NAME, createVerificationManagementDto.presentationDefinition().getName());
    }

    @Test
    void createVerification_BusinessException() {
        // given
        // mock useCaseCache
        UUID useCaseId = UUID.randomUUID();
        when(useCaseCache.getUseCaseById(useCaseId)).thenReturn(mockUseCase());

        // mock verifierAgentManagementClient
        UUID verificationId = UUID.randomUUID();
        String verificationUrlExpected = "URL";
        ManagementResponseDto managementResponseDto = ManagementResponseDto.builder()
            .id(verificationId)
                .state(VerificationStatusDto.SUCCESS)
            .verificationUrl(verificationUrlExpected)
            .build();
        when(verifierAgentManagementClient.createVerification(any())).thenReturn(managementResponseDto);

        // mock qrCodeService
        when(qrCodeService.create(verificationUrlExpected, 500)).thenThrow(ImageHandlingException.class);

        // when
        assertThrows(ImageHandlingException.class, () -> verificationService.createVerification(useCaseId));

        // then
        // assert useCaseService
        Mockito.verify(useCaseCache).getUseCaseById(useCaseId);
        Mockito.verify(verifierAgentManagementClient).createVerification(Mockito.any());
        Mockito.verify(qrCodeService).create(verificationUrlExpected, 500);
    }


    @Test
    void createVerification_OK() {
        // given
        // mock useCaseCache
        UUID useCaseId = UUID.randomUUID();
        when(useCaseCache.getUseCaseById(useCaseId)).thenReturn(mockUseCase());

        // mock verifierAgentManagementClient
        UUID verificationId = UUID.randomUUID();
        String verificationUrlExpected = "URL";
        ManagementResponseDto managementResponseDto = ManagementResponseDto.builder()
            .id(verificationId)
                .state(VerificationStatusDto.SUCCESS)
            .verificationUrl(verificationUrlExpected)
            .build();

        when(verifierAgentManagementClient.createVerification(any())).thenReturn(managementResponseDto);

        // mock qrCodeService
        byte[] imageBytes = "image".getBytes();
        String imageFormat = "format";
        when(qrCodeService.create(verificationUrlExpected, 500)).thenReturn(QrCode.builder().imageData(imageBytes).format(imageFormat).build());

        // when
        VerificationBeginResponseDto result = verificationService.createVerification(useCaseId);

        // then
        // assert result
        assertEquals(verificationId, result.getId());
        assertEquals(imageBytes, result.getQrCode());
        assertEquals(imageFormat, result.getQrCodeFormat());

        // assert useCaseService
        Mockito.verify(useCaseCache).getUseCaseById(useCaseId);

        // assert CreateVerificationManagementDto
        ArgumentCaptor<CreateVerificationManagementDto> createVerificationManagementDtoArgumentCaptor = ArgumentCaptor.forClass(CreateVerificationManagementDto.class);
        Mockito.verify(verifierAgentManagementClient).createVerification(createVerificationManagementDtoArgumentCaptor.capture());
        CreateVerificationManagementDto createVerificationManagementDto = createVerificationManagementDtoArgumentCaptor.getValue();
        assertIterableEquals(List.of("$.attribute2"), createVerificationManagementDto.presentationDefinition().getInputDescriptors().getFirst().constraints().fields().getFirst().path());
        assertIterableEquals(List.of("$.attribute"), createVerificationManagementDto.presentationDefinition().getInputDescriptors().getFirst().constraints().fields().get(1).path());
        assertEquals(CLIENT_NAME, createVerificationManagementDto.presentationDefinition().getName());

        // assert QrCode
        ArgumentCaptor<String> qrCodeArgumentCaptor = ArgumentCaptor.forClass(String.class);
        Mockito.verify(qrCodeService).create(qrCodeArgumentCaptor.capture(), eq(500));
        String verificationUrlActual = qrCodeArgumentCaptor.getValue();
        assertEquals(verificationUrlExpected, verificationUrlActual);
    }

    @Test
    void getVerificationStatus_throwsTechAdapterException() {
        when(verifierAgentManagementClient.getVerificationStatus(any())).thenThrow(VAMException.class);

        UUID verificationId = UUID.randomUUID();
        assertThrows(VAMException.class, () -> verificationService.getVerificationStatus(verificationId));
    }

    @Test
    void getVerificationStatus() {
        var uuid = UUID.randomUUID();
        var responseDto = ManagementResponseDto.builder()
                .state(VerificationStatusDto.SUCCESS)
            .build();

        // mock verifierAgentManagementClient
        when(verifierAgentManagementClient.getVerificationStatus(uuid)).thenReturn(responseDto);

        verificationService.getVerificationStatus(uuid);

        Mockito.verify(verifierAgentManagementClient).getVerificationStatus(uuid);
        Mockito.verify(loggingService).logVerificationResponse(responseDto);
    }

    private UseCase mockUseCase() {
        return UseCase.builder()
            .attributeGroup(AttributeGroup.builder()
                .attribute(Attribute.builder().name("attribute").order(2L).build())
                .attribute(Attribute.builder().name("attribute2").order(1L).build())
                .build())
            .build();
    }
}