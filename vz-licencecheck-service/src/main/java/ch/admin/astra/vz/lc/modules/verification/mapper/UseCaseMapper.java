package ch.admin.astra.vz.lc.modules.verification.mapper;

import ch.admin.astra.vz.lc.api.verification.model.UseCaseDto;
import ch.admin.astra.vz.lc.modules.verification.domain.usecase.UseCase;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(uses = AttributeGroupMapper.class)
public interface UseCaseMapper {

    @Mapping(source = "attributeGroups", target = "attributeGroups")
    UseCaseDto map(UseCase useCase);

    List<UseCaseDto> map(List<UseCase> credentials);
}
