package ch.admin.astra.vz.lc.modules.verification.domain.usecase;

import lombok.Builder;
import lombok.Singular;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Value
@Builder
@Jacksonized
public class InputElement {
    String type;
    String name;
    @Singular
    List<InputOption> inputOptions;
}

