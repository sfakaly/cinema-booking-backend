package io.github.sfakaly.catalog.movie;

import io.github.sfakaly.catalog.movie.dto.MovieCreateRequest;
import io.github.sfakaly.catalog.movie.dto.MoviePatchRequest;
import io.github.sfakaly.catalog.movie.dto.MovieResponse;
import io.github.sfakaly.config.CentralMappingConfig;
import org.mapstruct.*;

@Mapper(config = CentralMappingConfig.class)
public interface MovieMapper {

    @Mapping(target = "id", ignore = true)
    Movie toEntity(MovieCreateRequest dto);

    MovieResponse toResponse(Movie movie);

    @Mapping(target = "id", ignore = true)
    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromRequest(MoviePatchRequest dto, @MappingTarget Movie movie);
}
