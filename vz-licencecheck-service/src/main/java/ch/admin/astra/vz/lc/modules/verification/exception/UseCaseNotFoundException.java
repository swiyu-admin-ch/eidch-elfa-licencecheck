package ch.admin.astra.vz.lc.modules.verification.exception;

import lombok.Getter;

import java.util.UUID;

@Getter
public class UseCaseNotFoundException extends RuntimeException {

    private final UUID useCaseId;

    public UseCaseNotFoundException(UUID useCaseId) {
        this.useCaseId = useCaseId;
    }
}
