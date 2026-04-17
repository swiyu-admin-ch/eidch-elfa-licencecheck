package ch.admin.astra.vz.lc.modules.verification.service;

import ch.admin.astra.vz.controller.verifier.model.CreateVerificationManagementDto;
import ch.admin.astra.vz.controller.verifier.model.ManagementResponseDto;
import ch.admin.astra.vz.controller.verifier.model.VerificationStatusDto;
import ch.admin.astra.vz.lc.core.logging.LoggingService;
import ch.admin.astra.vz.lc.integration.verifierservice.client.VerifierServiceClient;
import ch.admin.astra.vz.lc.integration.verifierservice.exception.VerifierException;
import ch.admin.astra.vz.lc.modules.verification.domain.qrcode.QrCode;
import ch.admin.astra.vz.lc.modules.verification.domain.usecase.Attribute;
import ch.admin.astra.vz.lc.modules.verification.domain.usecase.AttributeGroup;
import ch.admin.astra.vz.lc.modules.verification.domain.usecase.UseCase;
import ch.admin.astra.vz.lc.modules.verification.exception.ImageHandlingException;
import ch.admin.astra.vz.lc.modules.verification.exception.UseCaseNotFoundException;
import ch.admin.astra.vz.lc.modules.verification.mapper.UseCaseMapper;
import ch.admin.astra.vz.lc.modules.verification.mapper.VerificationMapper;
import ch.admin.astra.vz.lc.modules.verification.service.qrcode.QrCodeService;
import ch.admin.astra.vz.lc.modules.verification.service.usecase.UseCaseCache;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class VerificationServiceTest {

    public static final String CLIENT_NAME = "Client";
    public static final String ALLOWED_ISSUER_DID = "did:example:123";
    public static final String VERIFICATION_DEEPLINK_EXPECTED = "DEEPLINK";

    @Mock
    private UseCaseCache useCaseCache;

    @Mock
    private UseCaseMapper useCaseMapper;

    @Mock
    private VerifierServiceClient verifierServiceClient;

    @Mock
    private VerificationMapper verificationMapper;

    @Mock
    private QrCodeService qrCodeService;

    @Spy
    private LoggingService loggingService;

    private final UUID useCaseId = UUID.randomUUID();
    private final UUID verificationId = UUID.randomUUID();
    private VerificationService verificationService;

    @BeforeEach
    void setUp() {
        verificationService = new VerificationService(
            useCaseCache, useCaseMapper, verifierServiceClient, verificationMapper,
            qrCodeService, loggingService, CLIENT_NAME, ALLOWED_ISSUER_DID
        );
    }

    @Test
    void testGetUseCases_callsCache() {
        // when
        verificationService.getUseCases();
        // then
        then(useCaseCache).should().getUseCases();
    }

    @Test
    void createVerification_UseCaseNotFoundException() {
        // given
        given(useCaseCache.getUseCaseById(useCaseId)).willThrow(UseCaseNotFoundException.class);

        // when
        final ThrowingCallable throwingCallable = () -> verificationService.createVerification(useCaseId);

        // then
        assertThatExceptionOfType(UseCaseNotFoundException.class).isThrownBy(throwingCallable);
        then(useCaseCache).should().getUseCaseById(useCaseId);
    }

    @Test
    void createVerification_TechAdapterException() {
        // given
        given(useCaseCache.getUseCaseById(useCaseId)).willReturn(mockUseCase());
        given(verifierServiceClient.createVerification(any())).willThrow(VerifierException.class);

        // when
        final ThrowingCallable throwingCallable = () -> verificationService.createVerification(useCaseId);

        // then
        assertThatExceptionOfType(VerifierException.class).isThrownBy(throwingCallable);
        then(useCaseCache).should().getUseCaseById(useCaseId);

        final var argumentCaptor = ArgumentCaptor.forClass(CreateVerificationManagementDto.class);
        then(verifierServiceClient).should().createVerification(argumentCaptor.capture());

        final var presentationDefinition = argumentCaptor.getValue().getPresentationDefinition();
        assertThat(presentationDefinition).isNotNull();
        assertThat(presentationDefinition.getName()).isEqualTo(CLIENT_NAME);

        final var inputDescriptorConstraintFields = presentationDefinition.getInputDescriptors().getFirst().getConstraints().getFields();
        assertThat(inputDescriptorConstraintFields.getFirst().getPath()).containsExactly("$.attribute2");
        assertThat(inputDescriptorConstraintFields.get(1).getPath()).containsExactly("$.attribute");
    }

    @Test
    void createVerification_BusinessException() {
        // given
        given(useCaseCache.getUseCaseById(useCaseId)).willReturn(mockUseCase());
        given(verifierServiceClient.createVerification(any())).willReturn(getManagementResponseDto());
        given(qrCodeService.create(VERIFICATION_DEEPLINK_EXPECTED, 500)).willThrow(ImageHandlingException.class);

        // when
        final ThrowingCallable throwingCallable = () -> verificationService.createVerification(useCaseId);

        // then
        assertThatExceptionOfType(ImageHandlingException.class).isThrownBy(throwingCallable);
        then(useCaseCache).should().getUseCaseById(useCaseId);
        then(verifierServiceClient).should().createVerification(Mockito.any());
        then(qrCodeService).should().create(VERIFICATION_DEEPLINK_EXPECTED, 500);
    }


    @Test
    void createVerification_OK() {
        // given
        given(useCaseCache.getUseCaseById(useCaseId)).willReturn(mockUseCase());
        given(verifierServiceClient.createVerification(any())).willReturn(getManagementResponseDto());

        // mock QrCodeService
        final var imageBytes = "image".getBytes();
        final var imageFormat = "format";
        given(qrCodeService.create(VERIFICATION_DEEPLINK_EXPECTED, 500))
            .willReturn(QrCode.builder().imageData(imageBytes).format(imageFormat).build());

        // when
        final var result = verificationService.createVerification(useCaseId);

        // then
        assertThat(result.id()).isEqualTo(verificationId);
        assertThat(result.qrCode()).isEqualTo(imageBytes);
        assertThat(result.qrCodeFormat()).isEqualTo(imageFormat);

        // assert useCaseService
        then(useCaseCache).should().getUseCaseById(useCaseId);

        // assert CreateVerificationManagementDto
        final var argumentCaptor = ArgumentCaptor.forClass(CreateVerificationManagementDto.class);
        then(verifierServiceClient).should().createVerification(argumentCaptor.capture());

        final var createVerificationManagementDto = argumentCaptor.getValue();
        final var presentationDefinition = createVerificationManagementDto.getPresentationDefinition();
        assertThat(presentationDefinition).isNotNull();

        final var inputDescriptorConstraintFields = presentationDefinition.getInputDescriptors().getFirst().getConstraints().getFields();
        assertThat(inputDescriptorConstraintFields.getFirst().getPath()).containsExactly("$.attribute2");
        assertThat(inputDescriptorConstraintFields.get(1).getPath()).containsExactly("$.attribute");
        assertThat(createVerificationManagementDto.getPresentationDefinition().getName()).isEqualTo(CLIENT_NAME);

        // assert QrCode
        final var qrCodeArgumentCaptor = ArgumentCaptor.forClass(String.class);
        then(qrCodeService).should().create(qrCodeArgumentCaptor.capture(), eq(500));
        assertThat(qrCodeArgumentCaptor.getValue()).isEqualTo(VERIFICATION_DEEPLINK_EXPECTED);
    }

    @Test
    void getVerificationStatus_throwsTechAdapterException() {
        given(verifierServiceClient.getVerificationStatus(any())).willThrow(VerifierException.class);

        assertThatExceptionOfType(VerifierException.class)
            .isThrownBy(() -> verificationService.getVerificationStatus(verificationId));
    }

    @Test
    void getVerificationStatus() {
        // given
        final var managementResponse = getManagementResponseDto();
        given(verifierServiceClient.getVerificationStatus(verificationId)).willReturn(managementResponse);

        // when
        verificationService.getVerificationStatus(verificationId);

        // then
        then(verifierServiceClient).should().getVerificationStatus(verificationId);
        then(loggingService).should().addVerificationStatusContext(managementResponse);
    }

    private ManagementResponseDto getManagementResponseDto() {
        return ManagementResponseDto.builder()
            .id(verificationId)
            .state(VerificationStatusDto.SUCCESS)
            .verificationUrl("URL")
            .verificationDeeplink(VERIFICATION_DEEPLINK_EXPECTED)
            .build();
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
