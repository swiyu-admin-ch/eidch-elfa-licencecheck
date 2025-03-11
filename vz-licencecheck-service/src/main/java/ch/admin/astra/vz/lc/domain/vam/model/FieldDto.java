package ch.admin.astra.vz.lc.domain.vam.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nonnull;
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
public class FieldDto {

    @Singular
    @JsonProperty("path")
    @Nonnull
    private List<String> paths;

    private String id;

    private String name;

    private String purpose;
}

