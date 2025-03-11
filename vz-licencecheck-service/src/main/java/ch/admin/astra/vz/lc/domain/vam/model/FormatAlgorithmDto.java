package ch.admin.astra.vz.lc.domain.vam.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Singular;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@Jacksonized
public class FormatAlgorithmDto {

    @Singular
    @JsonProperty("sd-jwt_alg_values")
    private List<String> algorithms;

    @Singular
    @JsonProperty("kb-jwt_alg_values")
    private List<String> keyBindingAlgorithms;
}

