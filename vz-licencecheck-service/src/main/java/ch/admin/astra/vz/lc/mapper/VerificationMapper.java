package ch.admin.astra.vz.lc.mapper;

import ch.admin.astra.vz.lc.api.HolderResponse;
import ch.admin.astra.vz.lc.api.VerificationBeginResponse;
import ch.admin.astra.vz.lc.api.VerificationStatusResponse;
import ch.admin.astra.vz.lc.domain.vam.model.ManagementResponseDto;
import ch.admin.astra.vz.lc.domain.vam.model.ResponseDataDto;
import ch.admin.astra.vz.lc.domain.verifier.model.VerificationBeginResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = StatusMapper.class)
public interface VerificationMapper {

    VerificationBeginResponse map(VerificationBeginResponseDto verificationBeginResponseDto);

    @Mapping(source = "state", target = "status")
    @Mapping(source = "walletResponse.errorCode", target = "errorCode")
    @Mapping(source = "walletResponse.errorDescription", target = "errorDescription")
    @Mapping(source = "walletResponse", target = "holderResponse")
    VerificationStatusResponse map(ManagementResponseDto managementResponseDto);

    default HolderResponse map(ResponseDataDto responseDataDto) {
        if (responseDataDto == null || responseDataDto.getCredentialSubject() == null) {
            return null;
        }

        return HolderResponse.builder()
                .attributes(responseDataDto.getCredentialSubject())
                .build();
    }
}