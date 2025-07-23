package ch.admin.astra.vz.lc.integration.verifiermanagement.client.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;

import java.util.List;

@Schema(name = "Field")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public record FieldDto(
        @NotEmpty
        @Schema(description = "(Mandatory) Array of one or more JSONPath string expressions")
        List<String> path,

        @Schema(description = "(Optional) If present value MUST be a string that is unique")
        String id,

        @Schema(description = "(Optional) If present human-friendly name which describes the target field")
        String name,

        @Schema(description = "(Optional) If present describes purpose for which the field is requested")
        String purpose
) {
}
