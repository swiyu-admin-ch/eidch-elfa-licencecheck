package ch.admin.astra.vz.lc.modules.verification.mapper;

import ch.admin.astra.vz.controller.verifier.model.ManagementResponseDto;
import ch.admin.astra.vz.controller.verifier.model.ResponseDataDto;
import ch.admin.astra.vz.lc.api.verification.model.HolderAttributesDto;
import ch.admin.astra.vz.lc.api.verification.model.VerificationStateDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.Map;

@Mapper(uses = StatusMapper.class)
public interface VerificationMapper {

    @Mapping(source = "state", target = "status")
    @Mapping(source = "walletResponse.errorCode", target = "errorCode")
    @Mapping(source = "walletResponse.errorDescription", target = "errorDescription")
    @Mapping(source = "walletResponse", target = "holderAttributes")
    VerificationStateDto map(ManagementResponseDto managementResponseDto);

    default HolderAttributesDto map(ResponseDataDto responseDataDto) {
        if (responseDataDto == null || responseDataDto.getCredentialSubjectData() == null) {
            return null;
        }

        Map<String, Object> data = responseDataDto.getCredentialSubjectData();

        if (data.size() == 1) {
            Object value = data.values().iterator().next();
            if (value instanceof List<?> list
                    && !list.isEmpty()
                    && list.getFirst() instanceof Map<?, ?> attrs) {
                return new HolderAttributesDto((Map<String, Object>) attrs);
            }
        }

        return new HolderAttributesDto(data);
    }
}