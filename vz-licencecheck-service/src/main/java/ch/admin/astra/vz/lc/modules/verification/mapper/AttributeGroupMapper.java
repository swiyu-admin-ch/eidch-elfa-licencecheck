package ch.admin.astra.vz.lc.modules.verification.mapper;

import ch.admin.astra.vz.lc.api.verification.model.AttributeGroupDto;
import ch.admin.astra.vz.lc.modules.verification.domain.usecase.AttributeGroup;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper
public interface AttributeGroupMapper {

    @Mapping(source = "attributes", target = "attributes")
    AttributeGroupDto map(AttributeGroup useCase);

    List<AttributeGroupDto> map(List<AttributeGroup> credentials);
}
