package ch.admin.astra.vz.lc.modules.verification.domain.usecase;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class InputOption {
    String key;
    String value;
    String imageFilePath;
}

