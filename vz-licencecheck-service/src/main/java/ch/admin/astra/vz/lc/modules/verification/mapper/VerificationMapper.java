package ch.admin.astra.vz.lc.modules.verification.mapper;

import ch.admin.astra.vz.lc.api.verification.model.HolderAttributesDto;
import ch.admin.astra.vz.lc.api.verification.model.VerificationStateDto;
import ch.admin.astra.vz.lc.integration.verifiermanagement.client.model.ManagementResponseDto;
import ch.admin.astra.vz.lc.integration.verifiermanagement.client.model.ResponseDataDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = StatusMapper.class)
public interface VerificationMapper {

    @Mapping(source = "state", target = "status")
    @Mapping(source = "walletResponse.errorCode", target = "errorCode")
    @Mapping(source = "walletResponse.errorDescription", target = "errorDescription")
    @Mapping(source = "walletResponse", target = "holderAttributes")
    VerificationStateDto map(ManagementResponseDto managementResponseDto);

    default HolderAttributesDto map(ResponseDataDto responseDataDto) {
        if (responseDataDto == null || responseDataDto.getCredentialSubject() == null) {
            return null;
        }

        return new HolderAttributesDto(responseDataDto.getCredentialSubject());
    }
}