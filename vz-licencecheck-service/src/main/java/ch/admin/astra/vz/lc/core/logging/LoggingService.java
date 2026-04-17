package ch.admin.astra.vz.lc.core.logging;

import ch.admin.astra.vz.controller.verifier.model.ManagementResponseDto;
import ch.admin.astra.vz.controller.verifier.model.VerificationStatusDto;
import ch.admin.astra.vz.lc.integration.verifierservice.exception.VerifierException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static ch.admin.astra.vz.lc.core.logging.LoggingUtil.OPERATION_FINISHED;
import static ch.admin.astra.vz.lc.core.logging.LoggingUtil.OPERATION_FINISHED_WITH_ERROR;
import static ch.admin.astra.vz.lc.core.logging.LoggingUtil.RequestStatus.ERROR;
import static ch.admin.astra.vz.lc.core.logging.LoggingUtil.RequestStatus.SUCCESS;
import static ch.admin.astra.vz.lc.core.logging.Operation.*;


@Slf4j
@Component
@RequiredArgsConstructor
public class LoggingService {

    public void addGetUseCasesContext() {
        LoggingUtil.setOperation(GET_USE_CASES);
    }

    public void addStartVerificationContext(UUID usecaseId) {
        LoggingUtil.setOperation(START_VERIFICATION_PROCESS);
        LoggingUtil.setUseCaseId(usecaseId);
        LoggingUtil.setVerificationStatus(VerificationStatusDto.PENDING.toString());
    }

    public void addGetVerificationProcessContext(UUID verificationId) {
        LoggingUtil.setOperation(GET_VERIFICATION_PROCESS_STATUS);
        LoggingUtil.setVerificationId(verificationId);
    }

    public void addVerificationStatusContext(UUID usecaseId, ManagementResponseDto response) {
        LoggingUtil.setUseCaseId(usecaseId);
        addVerificationStatusContext(response);
    }

    public void addVerificationStatusContext(ManagementResponseDto response) {
        LoggingUtil.setVerificationId(response.getId());
        LoggingUtil.setVerificationStatus(response.getState().toString());
        if (VerificationStatusDto.FAILED.toString().equals(response.getState())) {
            LoggingUtil.setErrorCode(response.getWalletResponse().getErrorCode());
        }
    }

    public void clearMDC() {
        MDC.clear();
    }


    public void logOperationFinished() {
        LoggingUtil.setStatus(SUCCESS);
        log.info(OPERATION_FINISHED);
    }

    public void logException(Exception e) {
        LoggingUtil.setStatus(ERROR);

        // Verifier exceptions can be business or system errors
        if (e instanceof VerifierException verifierException) {
            if(verifierException.getIsBusinessError()) {
                log.info(OPERATION_FINISHED, e);
            } else {
                String errorMessage = "%s ResponseStatusCode: %s Reason: %s"
                        .formatted(OPERATION_FINISHED_WITH_ERROR, verifierException.getStatus(), verifierException.getMessage());
                log.error(errorMessage, verifierException);
            }
        }
        // All other exceptions are system errors
        else {
            log.error("%s Reason: %s".formatted(OPERATION_FINISHED_WITH_ERROR, e.getMessage()), e);
        }
    }

}
