package ch.admin.astra.vz.lc.modules.verification.service;

import ch.admin.astra.vz.controller.verifier.model.*;
import ch.admin.astra.vz.lc.api.verification.model.UseCaseDto;
import ch.admin.astra.vz.lc.api.verification.model.VerificationBeginResponseDto;
import ch.admin.astra.vz.lc.api.verification.model.VerificationStateDto;
import ch.admin.astra.vz.lc.core.logging.LoggingService;
import ch.admin.astra.vz.lc.integration.verifierservice.client.VerifierServiceClient;
import ch.admin.astra.vz.lc.modules.verification.domain.qrcode.QrCode;
import ch.admin.astra.vz.lc.modules.verification.domain.usecase.UseCase;
import ch.admin.astra.vz.lc.modules.verification.mapper.UseCaseMapper;
import ch.admin.astra.vz.lc.modules.verification.mapper.VerificationMapper;
import ch.admin.astra.vz.lc.modules.verification.service.qrcode.QrCodeService;
import ch.admin.astra.vz.lc.modules.verification.service.usecase.UseCaseCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Component
public class VerificationService {

    private final UseCaseCache useCaseCache;

    private final UseCaseMapper useCaseMapper;
    private final VerificationMapper verificationMapper;

    private final VerifierServiceClient verifierServiceClient;
    private final QrCodeService qrCodeService;
    private final LoggingService loggingService;
    private final String clientName;
    private final String allowedIssuerDid;
    static final String DEFAULT_PURPOSE = "eLfa Verification";
    static final String DEFAULT_NAME = "eLfa";
    static final String DEFAULT_FORMAT_KEY = "vc+sd-jwt";
    static final String DEFAULT_PROOF_TYPE = "ES256";
    static final String DEFAULT_FORMATTED_PATH = "$.%s";

    public VerificationService(
            UseCaseCache useCaseCache,
            UseCaseMapper useCaseMapper,
            VerifierServiceClient verifierServiceClient,
            VerificationMapper verificationMapper,
            QrCodeService qrCodeService,
            LoggingService loggingService,
            @Value("${verifier-service.client-name}") String clientName,
            @Value("${verifier-service.allowed-issuer-did}") String allowedIssuerDid) {
        this.useCaseCache = useCaseCache;
        this.useCaseMapper = useCaseMapper;
        this.verificationMapper = verificationMapper;
        this.verifierServiceClient = verifierServiceClient;
        this.qrCodeService = qrCodeService;
        this.loggingService = loggingService;
        this.clientName = clientName;
        this.allowedIssuerDid = allowedIssuerDid;
    }

    private static final String VCT_PREFIX = "urn:vct:ch.admin.astra.vz.";

    public List<UseCaseDto> getUseCases() {
        List<UseCase> useCases = useCaseCache.getUseCases();
        return useCaseMapper.map(useCases);
    }

    protected UseCase getUseCase(UUID useCaseId) {
        return useCaseCache.getUseCaseById(useCaseId);
    }

    public VerificationBeginResponseDto createVerification(UUID useCaseId) {
        log.debug("Load use-case (DCQL mode)");
        UseCase useCase = getUseCase(useCaseId);
        List<String> attributeList = useCase.getSortedAttributes();
        String useCaseType = useCase.getType();

        log.debug("Build DCQL-based CreateVerificationManagementDto for type '{}'", useCaseType);
        CreateVerificationManagementDto request = buildDcqlRequest(attributeList, useCaseType);

        log.debug("Start verification process on Verifier Service (DCQL)");
        ManagementResponseDto response = verifierServiceClient.createVerification(request);

        log.debug("Create QR-Code");
        QrCode qrCode = qrCodeService.create(response.getVerificationDeeplink(), 500);

        var responseDto = new VerificationBeginResponseDto(response.getId(), qrCode.getImageData(), qrCode.getFormat());

        loggingService.addVerificationStatusContext(useCaseId, response);
        log.debug("Verification created successfully with ID: {} (DCQL)", response.getId());

        return responseDto;
    }

    public VerificationStateDto getVerificationStatus(UUID verificationId) {
        log.debug("Retrieving verification status for ID: {}", verificationId);
        ManagementResponseDto managementResponseDto = verifierServiceClient.getVerificationStatus(verificationId);
        loggingService.addVerificationStatusContext(managementResponseDto);
        log.debug("Verification status retrieved: {}", managementResponseDto);

        return verificationMapper.map(managementResponseDto);
    }

    private CreateVerificationManagementDto buildDcqlRequest(List<String> attributeList, String useCaseType) {
        return CreateVerificationManagementDto.builder()
                .acceptedIssuerDids(List.of(allowedIssuerDid))
                .jwtSecuredAuthorizationRequest(true)
                .responseMode(ResponseModeTypeDto.DIRECT_POST)
                .presentationDefinition(buildPresentationDefinition(attributeList))
                .dcqlQuery(buildDcqlQuery(attributeList, useCaseType))
                .build();
    }

    /**
     * Builds an empty PresentationDefinitionDto as required placeholder when using DCQL.
     * Use once Android Wallet can Support DCQL
     */
    private PresentationDefinitionDto buildEmptyPresentationDefinition() {
        return PresentationDefinitionDto.builder().build();
    }

    /**
     * Builds a full PresentationDefinitionDto for PE-based verification requests.
     * Remove once Android Wallet can Support DCQL
     */
    private PresentationDefinitionDto buildPresentationDefinition(List<String> attributeList) {
        return PresentationDefinitionDto.builder()
                .id(UUID.randomUUID().toString())
                .name(clientName)
                .purpose(DEFAULT_PURPOSE)
                .inputDescriptors(List.of(
                            InputDescriptorDto.builder()
                                    .id(UUID.randomUUID().toString())
                                    .name(DEFAULT_NAME)
                                    .format(Map.of(DEFAULT_FORMAT_KEY,
                                            FormatAlgorithmDto.builder().
                                                    sdJwtAlgValues(List.of(DEFAULT_PROOF_TYPE))
                                                    .kbJwtAlgValues(List.of(DEFAULT_PROOF_TYPE))
                                            .build()))
                                    .constraints(ConstraintDto.builder()
                                            .fields(attributeList.stream()
                                                    .map(attribute -> FieldDto.builder()
                                                            .path(List.of(String.format(DEFAULT_FORMATTED_PATH, attribute)))
                                                            .build())
                                                    .toList()).build())
                                    .build()))
                .build();
    }

    private DcqlQueryDtoDto buildDcqlQuery(List<String> attributeList, String useCaseType) {
        var claims = attributeList.stream()
                .map(attribute -> new DcqlClaimDtoDto()
                        .path(List.of(attribute)))
                .toList();

        String vctValue = VCT_PREFIX + useCaseType;

        var meta = new DcqlCredentialMetaDtoDto()
                .vctValues(List.of(vctValue));

        var credential = new DcqlCredentialDtoDto()
                .id(UUID.randomUUID().toString())
                .format("dc+sd-jwt")
                .claims(claims)
                .requireCryptographicHolderBinding(true)
                .meta(meta);

        return new DcqlQueryDtoDto()
                .credentials(List.of(credential));
    }
}

