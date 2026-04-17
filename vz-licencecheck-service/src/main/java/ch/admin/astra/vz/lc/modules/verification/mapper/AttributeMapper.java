package ch.admin.astra.vz.lc.modules.verification.mapper;

import ch.admin.astra.vz.lc.api.verification.model.AttributeDto;
import ch.admin.astra.vz.lc.modules.verification.domain.usecase.Attribute;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface AttributeMapper {
    AttributeDto map(Attribute attribute);
    List<AttributeDto> map(List<Attribute> attributes);
}
