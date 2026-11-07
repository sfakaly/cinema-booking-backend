package io.github.sfakaly.catalog.movie;

import io.github.sfakaly.config.CentralMappingConfig;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(config = CentralMappingConfig.class)
public interface MovieMapper {

    @Mapping(target = "id", ignore = true)
    Movie toEntity(MovieCreateRequest dto);

    MovieResponse toResponse(Movie movie);

    List<MovieResponse> toResponseList(List<Movie> movies);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromRequest(MovieCreateRequest dto, @MappingTarget Movie movie);
}
