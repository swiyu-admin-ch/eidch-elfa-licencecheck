package ch.admin.astra.vz.lc.util;

import ch.admin.astra.vz.lc.logging.Operation;
import org.junit.jupiter.api.Test;
import org.slf4j.MDC;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;


class LoggingUtilTest {

    @Test
    void test_setOperation() {
        // when
        LoggingUtil.setOperation(Operation.GET_USE_CASES);

        // then
        assertEquals(Operation.GET_USE_CASES.getOperationName(), MDC.get(LoggingUtil.KEY_OPERATION));
    }

    @Test
    void test_setUseCaseId() {
        var useCaseId = UUID.randomUUID();

        // when
        LoggingUtil.setUseCaseId(useCaseId);

        // then
        assertEquals(useCaseId.toString(), MDC.get(LoggingUtil.KEY_USE_CASE_ID));
    }

    @Test
    void test_setVerificationId() {
        // when
        UUID verificationId = UUID.randomUUID();
        LoggingUtil.setVerificationId(verificationId);

        // then
        assertEquals(verificationId.toString(), MDC.get(LoggingUtil.KEY_VERIFICATION_ID));
    }
}