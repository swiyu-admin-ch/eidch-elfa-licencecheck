package ch.admin.astra.vz.lc.mapper;

import ch.admin.astra.vz.lc.api.StatusDto;
import ch.admin.astra.vz.lc.domain.vam.model.VerificationState;
import org.mapstruct.Mapper;

@Mapper
public interface StatusMapper {

    StatusDto map(VerificationState verificationStatus);
}