package ch.admin.astra.vz.lc.modules.verification.service;

import ch.admin.astra.vz.lc.api.verification.model.UseCaseDto;
import ch.admin.astra.vz.lc.api.verification.model.VerificationBeginResponseDto;
import ch.admin.astra.vz.lc.api.verification.model.VerificationStateDto;
import ch.admin.astra.vz.lc.core.logging.LoggingService;
import ch.admin.astra.vz.lc.integration.verifiermanagement.client.VerifierAgentManagementClient;
import ch.admin.astra.vz.lc.integration.verifiermanagement.client.model.CreateVerificationManagementDto;
import ch.admin.astra.vz.lc.integration.verifiermanagement.client.model.ManagementResponseDto;
import ch.admin.astra.vz.lc.integration.verifiermanagement.client.model.PresentationDefinitionDto;
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
import java.util.UUID;

import static ch.admin.astra.vz.lc.integration.verifiermanagement.client.model.CreateVerificationManagementDto.buildCreateVerificationManagementDto;

@Slf4j
@Component
public class VerificationService {

    private final UseCaseCache useCaseCache;

    private final UseCaseMapper useCaseMapper;

    private final VerifierAgentManagementClient verifierAgentManagementClient;

    private final VerificationMapper verificationMapper;

    private final QrCodeService qrCodeService;

    private final LoggingService loggingService;

    private final String clientName;

    private final String allowedIssuerDid;

    public VerificationService(UseCaseCache useCaseCache,
                               UseCaseMapper useCaseMapper,
                               VerifierAgentManagementClient verifierAgentManagementClient,
                               VerificationMapper verificationMapper,
                               QrCodeService qrCodeService,
                               LoggingService loggingService,
                               @Value("${verifier.client-name}") String clientName,
                               @Value("${verifier.allowed-issuer-did}") String allowedIssuerDid) {
        this.useCaseCache = useCaseCache;
        this.useCaseMapper = useCaseMapper;
        this.verifierAgentManagementClient = verifierAgentManagementClient;
        this.verificationMapper = verificationMapper;
        this.qrCodeService = qrCodeService;
        this.loggingService = loggingService;
        this.clientName = clientName;
        this.allowedIssuerDid = allowedIssuerDid;
    }

    public List<UseCaseDto> getUseCases() {
        List<UseCase> useCases = useCaseCache.getUseCases();
        return useCaseMapper.map(useCases);
    }


    public VerificationBeginResponseDto createVerification(UUID useCaseId) {
        log.debug("Load use-case");
        List<String> attributeList = useCaseCache.getUseCaseById(useCaseId).getSortedAttributes();

        log.debug("Build CreateVerificationManagementDto");
        CreateVerificationManagementDto request = buildCreateVerificationManagementDto(PresentationDefinitionDto.buildPresentationDefinitionDto(clientName, attributeList), allowedIssuerDid, true);

        log.debug("Start verification Process on VAM");
        ManagementResponseDto response = verifierAgentManagementClient.createVerification(request);

        log.debug("Create QR-Code");
        QrCode qrCode = qrCodeService.create(response.verificationUrl(), 500);

        var responseDto = new VerificationBeginResponseDto(response.id(), qrCode.getImageData(), qrCode.getFormat());

        loggingService.addVerificationStatusContext(useCaseId, response);

        return responseDto;
    }

    public VerificationStateDto getVerificationStatus(UUID verificationId) {
        log.debug("Get verification status from VAM");
        ManagementResponseDto managementResponseDto = verifierAgentManagementClient.getVerificationStatus(verificationId);

        loggingService.addVerificationStatusContext(managementResponseDto);

        return verificationMapper.map(managementResponseDto);
    }
}
