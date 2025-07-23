package ch.admin.astra.vz.lc.integration.verifiermanagement.client.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class VerificationStateTest {

    @Test
    void isSuccess_true() {
        assertTrue(VerificationStatusDto.isSuccess(VerificationStatusDto.SUCCESS));
    }

    @ParameterizedTest
    @EnumSource(value = VerificationStatusDto.class, names = {"PENDING", "FAILED"})
    void isSuccess_false(VerificationStatusDto status) {
        assertFalse(VerificationStatusDto.isSuccess(status));
    }

    @Test
    void hasFailed_true() {
        assertTrue(VerificationStatusDto.hasFailed(VerificationStatusDto.FAILED));
    }

    @ParameterizedTest
    @EnumSource(value = VerificationStatusDto.class, names = {"PENDING", "SUCCESS"})
    void hasFailed_false(VerificationStatusDto status) {
        assertFalse(VerificationStatusDto.hasFailed(status));
    }


}