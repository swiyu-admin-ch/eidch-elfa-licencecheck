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
public class ConstraintDto {

    @Singular
    private List<FieldDto> fields;

    @JsonProperty("limit_disclosure")
    private String limitDisclosure;
}

