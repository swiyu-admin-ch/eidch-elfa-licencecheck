package ch.admin.astra.vz.lc.modules.verification.mapper;

import ch.admin.astra.vz.lc.api.verification.model.StatusDto;
import ch.admin.astra.vz.lc.integration.verifiermanagement.client.model.VerificationStatusDto;
import org.mapstruct.Mapper;

@Mapper
public interface StatusMapper {

    StatusDto map(VerificationStatusDto verificationStatus);
}