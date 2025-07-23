package ch.admin.astra.vz.lc.logging;

import ch.admin.astra.vz.lc.domain.vam.model.ManagementResponseDto;
import ch.admin.astra.vz.lc.domain.vam.model.VerificationStatusDto;
import ch.admin.astra.vz.lc.util.LoggingUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static ch.admin.astra.vz.lc.domain.vam.model.VerificationStatusDto.hasFailed;
import static ch.admin.astra.vz.lc.logging.Operation.*;
import static ch.admin.astra.vz.lc.util.LoggingUtil.OPERATION_FINISHED;
import static ch.admin.astra.vz.lc.util.LoggingUtil.OPERATION_FINISHED_WITH_ERROR;
import static ch.admin.astra.vz.lc.util.LoggingUtil.RequestStatus.ERROR;


@Slf4j
@Component
@RequiredArgsConstructor
public class LoggingService {

    public void initGetUseCases() {
        LoggingUtil.setOperation(GET_USE_CASES);
    }

    public void initStartVerification(UUID usecaseId) {
        LoggingUtil.setOperation(START_VERIFICATION_PROCESS);
        LoggingUtil.setUseCaseId(usecaseId);
        LoggingUtil.setVerificationStatus(VerificationStatusDto.PENDING);
    }

    public void initGetVerificationProcessStatus(UUID verificationId) {
        LoggingUtil.setOperation(GET_VERIFICATION_PROCESS_STATUS);
        LoggingUtil.setVerificationId(verificationId);
    }

    public void logStartVerificationResponse(UUID usecaseId, VerificationStatusDto state) {
        LoggingUtil.setUseCaseId(usecaseId);
        LoggingUtil.setVerificationStatus(state);
        logOperationFinished();
    }

    public void logException(Exception ex) {
        LoggingUtil.setStatus(ERROR);
        log.error("%s Reason: %s".formatted(OPERATION_FINISHED_WITH_ERROR, ex.toString()), ex);
    }

    public void operationFinished() {
        log.info(OPERATION_FINISHED);
    }

    public void logVerificationResponse(ManagementResponseDto response) {
        LoggingUtil.setVerificationStatus(response.state());
        if (hasFailed(response.state())) {
            LoggingUtil.setErrorCode(response.walletResponse().getErrorCode());
        }
        logOperationFinished();
    }

    private void logOperationFinished() {
        log.info(OPERATION_FINISHED);
    }
}
