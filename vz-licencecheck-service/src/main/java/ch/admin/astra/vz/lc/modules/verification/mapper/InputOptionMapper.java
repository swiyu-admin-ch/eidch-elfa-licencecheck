package ch.admin.astra.vz.lc.modules.verification.mapper;

import ch.admin.astra.vz.lc.api.verification.model.InputOptionDto;
import ch.admin.astra.vz.lc.core.util.ImageUtils;
import ch.admin.astra.vz.lc.modules.verification.domain.usecase.InputOption;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper
public interface InputOptionMapper {

    @Mapping(target = "image", source = "imageFilePath", qualifiedByName = "convertImageToBase64")
    InputOptionDto map(InputOption inputOption);

    List<InputOptionDto> map(List<InputOption> inputOptions);

    @Named("convertImageToBase64")
    default String convertImageToBase64(String imageFilePath) {
        return ImageUtils.convertToBase64(imageFilePath);
    }
}
