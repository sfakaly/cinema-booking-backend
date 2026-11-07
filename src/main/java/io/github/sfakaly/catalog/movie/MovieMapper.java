package io.github.sfakaly.catalog.movie;

import io.github.sfakaly.config.CentralMappingConfig;
import org.mapstruct.*;

import java.util.List;

@Mapper(config = CentralMappingConfig.class)
public interface MovieMapper {

    @Mapping(target = "id", ignore = true)
    Movie toEntity(MovieCreateRequest dto);

    MovieResponse toResponse(Movie movie);

    List<MovieResponse> toResponseList(List<Movie> movies);

    @Mapping(target = "id", ignore = true)
    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromRequest(MoviePatchRequest dto, @MappingTarget Movie movie);
}
