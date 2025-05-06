package ch.admin.astra.vz.lc.mapper;

import ch.admin.astra.vz.lc.api.HolderAttributesDto;
import ch.admin.astra.vz.lc.api.VerificationStartedDto;
import ch.admin.astra.vz.lc.api.VerificationStateDto;
import ch.admin.astra.vz.lc.domain.vam.model.ManagementResponseDto;
import ch.admin.astra.vz.lc.domain.vam.model.ResponseDataDto;
import ch.admin.astra.vz.lc.domain.verifier.model.VerificationBeginResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = StatusMapper.class)
public interface VerificationMapper {

    VerificationStartedDto map(VerificationBeginResponseDto verificationBeginResponseDto);

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