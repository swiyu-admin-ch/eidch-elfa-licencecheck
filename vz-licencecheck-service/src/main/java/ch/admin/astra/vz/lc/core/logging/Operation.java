package ch.admin.astra.vz.lc.core.logging;

import lombok.Getter;

@Getter
public enum Operation {
    GET_USE_CASES("Get use-cases"),
    START_VERIFICATION_PROCESS("Start verification process"),
    GET_VERIFICATION_PROCESS_STATUS("Get verification process status");

    private final String operationName;

    Operation(String operationName) {
        this.operationName = operationName;
    }
}
