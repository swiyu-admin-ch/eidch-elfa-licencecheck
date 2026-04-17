package ch.admin.astra.vz.lc.core.logging;

import org.junit.jupiter.api.Test;
import org.slf4j.MDC;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class LoggingUtilTest {

    @Test
    void test_setOperation() {
        // when
        LoggingUtil.setOperation(Operation.GET_USE_CASES);

        // then
        assertThat(MDC.get(LoggingUtil.KEY_OPERATION))
            .isEqualTo(Operation.GET_USE_CASES.getOperationName());
    }

    @Test
    void test_setUseCaseId() {
        var useCaseId = UUID.randomUUID();

        // when
        LoggingUtil.setUseCaseId(useCaseId);

        // then
        assertThat(MDC.get(LoggingUtil.KEY_USE_CASE_ID))
            .isEqualTo(useCaseId.toString());
    }

    @Test
    void test_setVerificationId() {
        // when
        UUID verificationId = UUID.randomUUID();
        LoggingUtil.setVerificationId(verificationId);

        // then
        assertThat(MDC.get(LoggingUtil.KEY_VERIFICATION_ID))
            .isEqualTo(verificationId.toString());
    }
}