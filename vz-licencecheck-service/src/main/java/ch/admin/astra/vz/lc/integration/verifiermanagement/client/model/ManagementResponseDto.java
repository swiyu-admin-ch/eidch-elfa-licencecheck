package ch.admin.astra.vz.lc.integration.verifiermanagement.client.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.UUID;

@Schema(name = "ManagementResponse")
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ManagementResponseDto(
        UUID id,
        @JsonProperty("request_nonce")
        String requestNonce,
        VerificationStatusDto state,
        @JsonProperty("presentation_definition")
        PresentationDefinitionDto presentationDefinition,
        @JsonProperty("wallet_response")
        ResponseDataDto walletResponse,
        @JsonProperty("verification_url")
        String verificationUrl
) {
}

