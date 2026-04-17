package ch.admin.astra.vz.lc.modules.verification.mapper;

import ch.admin.astra.vz.lc.api.verification.model.InputElementDto;
import ch.admin.astra.vz.lc.modules.verification.domain.usecase.InputElement;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(uses = InputOptionMapper.class)
public interface InputElementMapper {

    InputElementDto map(InputElement inputElement);

    List<InputElementDto> map(List<InputElement> inputElements);
}
