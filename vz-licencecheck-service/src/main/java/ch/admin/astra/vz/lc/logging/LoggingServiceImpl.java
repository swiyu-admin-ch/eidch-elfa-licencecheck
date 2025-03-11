package ch.admin.astra.vz.lc.logging;

import ch.admin.astra.vz.lc.domain.vam.model.ManagementResponseDto;
import ch.admin.astra.vz.lc.domain.vam.model.VerificationState;
import ch.admin.astra.vz.lc.util.LoggingUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static ch.admin.astra.vz.lc.domain.vam.model.VerificationState.hasFailed;
import static ch.admin.astra.vz.lc.logging.Operation.*;
import static ch.admin.astra.vz.lc.util.LoggingUtil.OPERATION_FINISHED;
import static ch.admin.astra.vz.lc.util.LoggingUtil.OPERATION_FINISHED_WITH_ERROR;
import static ch.admin.astra.vz.lc.util.LoggingUtil.RequestStatus.ERROR;


@Slf4j
@Component
@RequiredArgsConstructor
public class LoggingServiceImpl implements LoggingService {

    @Override
    public void initGetUseCases() {
        LoggingUtil.setOperation(GET_USE_CASES);
    }

    @Override
    public void initStartVerification(UUID usecaseId) {
        LoggingUtil.setOperation(START_VERIFICATION_PROCESS);
        LoggingUtil.setUseCaseId(usecaseId);
        LoggingUtil.setVerificationStatus(VerificationState.PENDING);
    }

    @Override
    public void initGetVerificationProcessStatus(UUID verificationId) {
        LoggingUtil.setOperation(GET_VERIFICATION_PROCESS_STATUS);
        LoggingUtil.setVerificationId(verificationId);
    }

    public void logStartVerificationResponse(UUID usecaseId, VerificationState state) {
        LoggingUtil.setUseCaseId(usecaseId);
        LoggingUtil.setVerificationStatus(state);
        logOperationFinished();
    }

    @Override
    public void logException(Exception ex) {
        LoggingUtil.setStatus(ERROR);
        log.error("%s Reason: %s".formatted(OPERATION_FINISHED_WITH_ERROR, ex.toString()), ex);
    }

    @Override
    public void operationFinished() {
        log.info(OPERATION_FINISHED);
    }

    @Override
    public void logVerificationResponse(ManagementResponseDto response) {
        LoggingUtil.setVerificationStatus(response.getState());
        if (hasFailed(response.getState())) {
            LoggingUtil.setErrorCode(response.getWalletResponse().getErrorCode());
        }
        logOperationFinished();
    }

    private void logOperationFinished() {
        log.info(OPERATION_FINISHED);
    }
}
