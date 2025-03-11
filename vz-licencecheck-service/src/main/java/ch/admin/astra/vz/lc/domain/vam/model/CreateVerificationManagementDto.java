package ch.admin.astra.vz.lc.domain.vam.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@AllArgsConstructor
@Jacksonized
public class CreateVerificationManagementDto {

    @JsonProperty("jwt_secured_authorization_request")
    private Boolean isJwtSecuredAuthorizationRequest;

    @JsonProperty("presentation_definition")
    private PresentationDefinitionDto presentationDefinition;

    public static CreateVerificationManagementDto buildCreateVerificationManagementDto(Boolean isJwtSecuredAuthorizationRequest, PresentationDefinitionDto presentationDefinition) {
        return CreateVerificationManagementDto.builder()
                .isJwtSecuredAuthorizationRequest(isJwtSecuredAuthorizationRequest)
                .presentationDefinition(presentationDefinition)
                .build();
    }
}
