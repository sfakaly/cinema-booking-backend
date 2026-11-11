package io.github.sfakaly.catalog.cinema;

import io.github.sfakaly.catalog.cinema.dto.CinemaCreateRequest;
import io.github.sfakaly.catalog.cinema.dto.CinemaPatchRequest;
import io.github.sfakaly.catalog.cinema.dto.CinemaResponse;
import io.github.sfakaly.config.CentralMappingConfig;
import org.mapstruct.*;

@Mapper(config = CentralMappingConfig.class)
public interface CinemaMapper {

    @Mapping(target = "id", ignore = true)
    Cinema toEntity(CinemaCreateRequest dto);

    CinemaResponse toResponse(Cinema cinema);

    @Mapping(target = "id", ignore = true)
    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void patchEntityFromRequest(CinemaPatchRequest dto, @MappingTarget Cinema cinema);
}
