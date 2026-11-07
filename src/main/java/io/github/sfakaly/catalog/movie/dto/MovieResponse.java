package io.github.sfakaly.catalog.movie.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.github.sfakaly.catalog.movie.AgeRating;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record MovieResponse (
        Long id,
        String title,
        String description,
        AgeRating ageRating,
        Integer durationSeconds
) {}
