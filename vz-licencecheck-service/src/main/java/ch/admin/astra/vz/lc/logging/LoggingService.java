package ch.admin.astra.vz.lc.logging;

import ch.admin.astra.vz.lc.domain.vam.model.ManagementResponseDto;
import ch.admin.astra.vz.lc.domain.vam.model.VerificationState;

import java.util.UUID;

public interface LoggingService {
    void initGetUseCases();

    void initStartVerification(UUID usecaseId);

    void initGetVerificationProcessStatus(UUID verificationId);

    void logStartVerificationResponse(UUID usecaseId, VerificationState state);

    void logVerificationResponse(ManagementResponseDto response);

    void logException(Exception ex);

    void operationFinished();
}
