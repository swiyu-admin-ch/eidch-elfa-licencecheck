package ch.admin.astra.vz.lc.domain.verifier.impl;

import ch.admin.astra.vz.lc.domain.qrcode.QrCodeService;
import ch.admin.astra.vz.lc.domain.qrcode.model.QrCode;
import ch.admin.astra.vz.lc.domain.vam.model.CreateVerificationManagementDto;
import ch.admin.astra.vz.lc.domain.vam.model.ManagementResponseDto;
import ch.admin.astra.vz.lc.domain.vam.model.PresentationDefinitionDto;
import ch.admin.astra.vz.lc.domain.vam.service.VerifierAgentManagementClient;
import ch.admin.astra.vz.lc.domain.verifier.UseCaseCache;
import ch.admin.astra.vz.lc.domain.verifier.VerifierService;
import ch.admin.astra.vz.lc.domain.verifier.model.UseCase;
import ch.admin.astra.vz.lc.domain.verifier.model.VerificationBeginResponseDto;
import ch.admin.astra.vz.lc.logging.LoggingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Slf4j
@Component
public class VerifierServiceImpl implements VerifierService {

    private final UseCaseCache useCaseCache;

    private final VerifierAgentManagementClient verifierAgentManagementClient;

    private final QrCodeService qrCodeService;

    private final LoggingService loggingService;

    private final String clientName;

    public VerifierServiceImpl(UseCaseCache useCaseCache,
                               VerifierAgentManagementClient verifierAgentManagementClient,
                               QrCodeService qrCodeService,
                               LoggingService loggingService,
                               @Value("${verifier.metaData.clientName}") String clientName) {
        this.useCaseCache = useCaseCache;
        this.verifierAgentManagementClient = verifierAgentManagementClient;
        this.qrCodeService = qrCodeService;
        this.loggingService = loggingService;
        this.clientName = clientName;
    }

    @Override
    public List<UseCase> getUseCases() {
        List<UseCase> useCases = useCaseCache.getUseCases();
        loggingService.operationFinished();
        return useCases;
    }

    @Override
    public VerificationBeginResponseDto createVerification(UUID useCaseId) {
        log.debug("Load use-case");
        List<String> attributeList = useCaseCache.getUseCaseById(useCaseId).getSortedAttributes();

        log.debug("Build CreateVerificationManagementDto");
        CreateVerificationManagementDto request = CreateVerificationManagementDto.buildCreateVerificationManagementDto(true, PresentationDefinitionDto.buildPresentationDefinitionDto(clientName, attributeList));

        log.debug("Start verification Process on VAM");
        ManagementResponseDto response = verifierAgentManagementClient.createVerification(request);

        log.debug("Create QR-Code");
        QrCode qrCode = qrCodeService.create(response.getVerificationUrl(), 500);

        var responseDto = VerificationBeginResponseDto.builder()
            .id(response.getId())
            .qrCode(qrCode.getImageData())
            .qrCodeFormat(qrCode.getFormat())
            .build();

        loggingService.logStartVerificationResponse(response.getId(), response.getState());

        return responseDto;
    }

    @Override
    public ManagementResponseDto getVerificationStatus(UUID verificationId) {
        log.debug("Get verification status from VAM");
        ManagementResponseDto response = verifierAgentManagementClient.getVerificationStatus(verificationId);

        loggingService.logVerificationResponse(response);

        return response;
    }
}
