package ch.admin.astra.vz.lc.integration.verifiermanagement.client.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum VerificationStatusDto {

    PENDING("PENDING"),

    SUCCESS("SUCCESS"),

    FAILED("FAILED");

    private final String value;

    public static boolean isSuccess(VerificationStatusDto status) {
        return SUCCESS.equals(status);
    }

    public static boolean hasFailed(VerificationStatusDto status) {
        return FAILED.equals(status);
    }
}
