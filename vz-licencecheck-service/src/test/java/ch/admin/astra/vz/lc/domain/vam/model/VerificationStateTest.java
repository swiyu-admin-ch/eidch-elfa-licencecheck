package ch.admin.astra.vz.lc.domain.vam.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class VerificationStateTest {

    @Test
    void isSuccess_true() {
        assertTrue(VerificationState.isSuccess(VerificationState.SUCCESS));
    }

    @ParameterizedTest
    @EnumSource(value = VerificationState.class, names = {"PENDING", "FAILED"})
    void isSuccess_false(VerificationState status) {
        assertFalse(VerificationState.isSuccess(status));
    }

    @Test
    void hasFailed_true() {
        assertTrue(VerificationState.hasFailed(VerificationState.FAILED));
    }

    @ParameterizedTest
    @EnumSource(value = VerificationState.class, names = {"PENDING", "SUCCESS"})
    void hasFailed_false(VerificationState status) {
        assertFalse(VerificationState.hasFailed(status));
    }


}