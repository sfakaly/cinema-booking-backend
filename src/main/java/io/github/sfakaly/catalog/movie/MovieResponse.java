package io.github.sfakaly.catalog.movie;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record MovieResponse (
        Long id,
        String title,
        String description,
        AgeRating ageRating,
        Integer durationSeconds
) {}
