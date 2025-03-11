package ch.admin.astra.vz.lc.domain.vam.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nonnull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Singular;
import lombok.extern.jackson.Jacksonized;

import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@Jacksonized
public class InputDescriptorDto {

    @Nonnull
    private String id;

    private String name;

    private String purpose;

    @Singular
    @JsonProperty("format")
    private Map<String, FormatAlgorithmDto> formats;

    @Nonnull
    private ConstraintDto constraints;
}

