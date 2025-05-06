package ch.admin.astra.vz.lc.mapper;

import ch.admin.astra.vz.lc.api.AttributeGroupDto;
import ch.admin.astra.vz.lc.domain.verifier.model.AttributeGroup;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper
public interface AttributeGroupMapper {

    @Mapping(source = "attributes", target = "attributes")
    AttributeGroupDto map(AttributeGroup useCase);

    List<AttributeGroupDto> map(List<AttributeGroup> credentials);
}
