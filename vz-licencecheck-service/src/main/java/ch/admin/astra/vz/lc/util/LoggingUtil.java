package ch.admin.astra.vz.lc.util;

import ch.admin.astra.vz.lc.domain.vam.model.ErrorCodeDto;
import ch.admin.astra.vz.lc.domain.vam.model.VerificationState;
import ch.admin.astra.vz.lc.logging.Operation;
import lombok.experimental.UtilityClass;
import org.slf4j.MDC;

import java.util.UUID;

@UtilityClass
public class LoggingUtil {

    public enum RequestStatus {
        SUCCESS,
        ERROR,
    }

    public static final String OPERATION_FINISHED = "Operation finished";
    public static final String OPERATION_FINISHED_WITH_ERROR = "Operation finished with error: ";
    public static final String KEY_OPERATION = "operation";
    public static final String KEY_VERIFICATION_ID = "verificationId";
    public static final String KEY_ERROR_CODE = "errorCode";
    public static final String KEY_USE_CASE_ID = "useCaseId";
    public static final String KEY_VERIFICATION_STATUS = "verificationStatus";

    public static void setOperation(Operation operation) {
        MDC.put(KEY_OPERATION, operation.getOperationName());
    }

    public static void setUseCaseId(UUID useCaseId) {
        MDC.put(KEY_USE_CASE_ID, useCaseId.toString());
    }

    public static void setVerificationId(UUID verificationId) {
        MDC.put(KEY_VERIFICATION_ID, verificationId.toString());
    }

    public static void setVerificationStatus(VerificationState state) {
        MDC.put(KEY_VERIFICATION_STATUS, state.name());
    }

    public static void setStatus(RequestStatus state) {
        MDC.put(KEY_VERIFICATION_STATUS, state.name());
    }

    public static void setErrorCode(ErrorCodeDto errorCode) {
        MDC.put(KEY_ERROR_CODE, errorCode.getValue());
    }
}
