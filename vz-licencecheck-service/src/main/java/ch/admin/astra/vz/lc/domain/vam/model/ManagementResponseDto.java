package ch.admin.astra.vz.lc.domain.vam.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@Jacksonized
public class ManagementResponseDto {

    private UUID id;

    private VerificationState state;

    @JsonProperty("request_nonce")
    private String requestNonce;

    @JsonProperty("presentation_definition")
    private PresentationDefinitionDto presentationDefinition;

    @JsonProperty("wallet_response")
    private ResponseDataDto walletResponse;

    @JsonProperty("verification_url")
    private String verificationUrl;
}

