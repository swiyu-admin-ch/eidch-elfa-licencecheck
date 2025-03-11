package ch.admin.astra.vz.lc.api;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.Singular;
import lombok.extern.jackson.Jacksonized;

import java.util.List;
import java.util.UUID;

@Data
@Jacksonized
@Builder(toBuilder = true)
@Schema(name = "UseCaseResponse")
public class UseCaseResponse {

    private UUID id;

    private String title;

    private String description;

    private Long order;

    @Singular
    private List<AttributeGroupResponse> attributeGroups;
}
