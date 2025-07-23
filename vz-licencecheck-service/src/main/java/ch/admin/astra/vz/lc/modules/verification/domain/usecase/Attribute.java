package ch.admin.astra.vz.lc.modules.verification.domain.usecase;


import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

@Getter
@Builder
@Jacksonized
public final class Attribute {

    private final String name;
    private final String type;
    private final Long order;
}
