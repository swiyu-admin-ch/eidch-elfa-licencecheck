package ch.admin.astra.vz.lc.domain.vam.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nonnull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Singular;
import lombok.extern.jackson.Jacksonized;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@Jacksonized
public class PresentationDefinitionDto {

    static final String DEFAULT_PURPOSE = "eLfa Verification";
    static final String DEFAULT_NAME = "eLfa";
    static final String DEFAULT_FORMAT_KEY = "vc+sd-jwt";
    static final String DEFAULT_PROOF_TYPE = "ES256";
    static final String DEFAULT_FORMATTED_PATH = "$.%s";

    private String id;
    private String name;
    private String purpose;

    @Singular
    @Nonnull
    @JsonProperty("input_descriptors")
    private List<InputDescriptorDto> inputDescriptors;

    public static PresentationDefinitionDto buildPresentationDefinitionDto(String clientName, List<String> attributeList) {
        return PresentationDefinitionDto.builder()
                .id(UUID.randomUUID().toString())
                .name(clientName)
                .purpose(DEFAULT_PURPOSE)
                .inputDescriptor(InputDescriptorDto.builder()
                        .id(UUID.randomUUID().toString())
                        .name(DEFAULT_NAME)
                        .format(DEFAULT_FORMAT_KEY, FormatAlgorithmDto.builder()
                                .algorithm(DEFAULT_PROOF_TYPE)
                                .keyBindingAlgorithm(DEFAULT_PROOF_TYPE)
                                .build())
                        .constraints(ConstraintDto.builder()
                                .fields(attributeList.stream().map(attribute -> FieldDto.builder()
                                                .paths(List.of(DEFAULT_FORMATTED_PATH.formatted(attribute)))
                                                .build())
                                        .toList()
                                )
                                .build())
                        .build())
                .build();
    }
}

