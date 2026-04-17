package ch.admin.astra.vz.lc.modules.verification.mapper;

import ch.admin.astra.vz.controller.verifier.model.VerificationStatusDto;
import ch.admin.astra.vz.lc.api.verification.model.StatusDto;
import org.mapstruct.Mapper;

@Mapper
public interface StatusMapper {

    StatusDto map(VerificationStatusDto verificationStatus);
}