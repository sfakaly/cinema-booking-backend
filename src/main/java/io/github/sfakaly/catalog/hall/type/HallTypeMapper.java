package io.github.sfakaly.catalog.hall.type;

import io.github.sfakaly.catalog.hall.type.dto.HallTypeResponse;
import io.github.sfakaly.config.CentralMappingConfig;
import org.mapstruct.Mapper;

@Mapper(config = CentralMappingConfig.class)
public interface HallTypeMapper {

    HallTypeResponse toResponse(HallType hallType);
}
