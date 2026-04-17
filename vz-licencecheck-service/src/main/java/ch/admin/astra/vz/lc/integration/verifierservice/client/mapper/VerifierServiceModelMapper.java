package ch.admin.astra.vz.lc.integration.verifierservice.client.mapper;

import ch.admin.astra.vz.controller.verifier.model.CreateVerificationManagementDto;
import ch.admin.astra.vz.controller.verifier.model.FormatAlgorithmDto;
import ch.admin.astra.vz.controller.verifier.model.ManagementResponseDto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * MapStruct mapper for converting between OpenAPI generated models and domain DTOs.
 */
@Mapper(componentModel = "spring")
public interface VerifierServiceModelMapper {

    /**
     * Convert domain DTO to OpenAPI model for request.
     */
    CreateVerificationManagementDto toOpenApiModel(ch.admin.astra.vz.lc.integration.verifierservice.client.model.CreateVerificationManagementDto dto);

    /**
     * Convert OpenAPI model to domain DTO for response.
     */
    ch.admin.astra.vz.lc.integration.verifierservice.client.model.ManagementResponseDto toDomainDto(ManagementResponseDto response);

    /**
     * Map FormatAlgorithmDto to FormatAlgorithm.
     * The field names are different:
     * - alg -> sdJwtAlgValues
     * - keyBindingAlg -> kbJwtAlgValues
     */
    @Mapping(source = "alg", target = "sdJwtAlgValues")
    @Mapping(source = "keyBindingAlg", target = "kbJwtAlgValues")
    FormatAlgorithmDto toOpenApiFormatAlgorithm(ch.admin.astra.vz.lc.integration.verifierservice.client.model.FormatAlgorithmDto dto);

    /**
     * Map FormatAlgorithm to FormatAlgorithmDto.
     */
    @Mapping(source = "sdJwtAlgValues", target = "alg")
    @Mapping(source = "kbJwtAlgValues", target = "keyBindingAlg")
    ch.admin.astra.vz.lc.integration.verifierservice.client.model.FormatAlgorithmDto toDomainFormatAlgorithm(FormatAlgorithmDto model);
}

