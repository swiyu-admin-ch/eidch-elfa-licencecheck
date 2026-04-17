package ch.admin.astra.vz.lc.modules.verification.exception;

import lombok.Getter;

import java.util.UUID;

@Getter
public class UseCaseNotFoundException extends RuntimeException {

    public static final String EXCEPTION_MSG = "use-case-not-found";

    private final UUID useCaseId;

    public UseCaseNotFoundException(UUID useCaseId) {
        this.useCaseId = useCaseId;
    }
}
