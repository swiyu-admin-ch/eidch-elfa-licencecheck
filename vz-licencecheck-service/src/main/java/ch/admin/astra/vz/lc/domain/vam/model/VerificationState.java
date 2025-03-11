package ch.admin.astra.vz.lc.domain.vam.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum VerificationState {

    PENDING("PENDING"),

    SUCCESS("SUCCESS"),

    FAILED("FAILED");

    private final String value;

    public static boolean isSuccess(VerificationState status) {
        return SUCCESS.equals(status);
    }

    public static boolean hasFailed(VerificationState status) {
        return FAILED.equals(status);
    }
}
