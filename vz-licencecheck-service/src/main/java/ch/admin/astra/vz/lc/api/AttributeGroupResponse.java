package ch.admin.astra.vz.lc.api;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.Singular;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Data
@Jacksonized
@Builder(toBuilder = true)
@Schema(name = "AttributeGroup")
public class AttributeGroupResponse {

        private String name;

        private Long order;

        @Singular
        private List<AttributeResponse> attributes;
}
