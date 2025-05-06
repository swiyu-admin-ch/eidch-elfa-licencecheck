package ch.admin.astra.vz.lc.domain.verifier;

import ch.admin.astra.vz.lc.api.UseCaseDto;
import ch.admin.astra.vz.lc.api.VerificationStateDto;
import ch.admin.astra.vz.lc.domain.qrcode.QrCodeService;
import ch.admin.astra.vz.lc.domain.qrcode.model.QrCode;
import ch.admin.astra.vz.lc.domain.vam.model.CreateVerificationManagementDto;
import ch.admin.astra.vz.lc.domain.vam.model.ManagementResponseDto;
import ch.admin.astra.vz.lc.domain.vam.model.PresentationDefinitionDto;
import ch.admin.astra.vz.lc.domain.vam.service.VerifierAgentManagementClient;
import ch.admin.astra.vz.lc.domain.verifier.model.UseCase;
import ch.admin.astra.vz.lc.domain.verifier.model.VerificationBeginResponseDto;
import ch.admin.astra.vz.lc.logging.LoggingService;
import ch.admin.astra.vz.lc.mapper.UseCaseMapper;
import ch.admin.astra.vz.lc.mapper.VerificationMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

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

    public VerificationService(UseCaseCache useCaseCache,
                               UseCaseMapper useCaseMapper,
                               VerifierAgentManagementClient verifierAgentManagementClient,
                               VerificationMapper verificationMapper,
                               QrCodeService qrCodeService,
                               LoggingService loggingService,
                               @Value("${verifier.metaData.clientName}") String clientName) {
        this.useCaseCache = useCaseCache;
        this.useCaseMapper = useCaseMapper;
        this.verifierAgentManagementClient = verifierAgentManagementClient;
        this.verificationMapper = verificationMapper;
        this.qrCodeService = qrCodeService;
        this.loggingService = loggingService;
        this.clientName = clientName;
    }

    public List<UseCaseDto> getUseCases() {
        List<UseCase> useCases = useCaseCache.getUseCases();
        loggingService.operationFinished();
        return useCaseMapper.map(useCases);
    }


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

    public VerificationStateDto getVerificationStatus(UUID verificationId) {
        log.debug("Get verification status from VAM");
        ManagementResponseDto response = verifierAgentManagementClient.getVerificationStatus(verificationId);

        loggingService.logVerificationResponse(response);

        return verificationMapper.map(response);
    }
}
