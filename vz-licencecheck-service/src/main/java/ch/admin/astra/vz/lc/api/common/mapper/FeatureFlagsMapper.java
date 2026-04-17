package ch.admin.astra.vz.lc.api.common.mapper;

import ch.admin.astra.vz.lc.api.common.model.FeatureFlagsDto;
import ch.admin.astra.vz.lc.core.features.FeaturesProperties;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FeatureFlagsMapper {

    FeatureFlagsDto toDto(FeaturesProperties featuresProperties);
}
