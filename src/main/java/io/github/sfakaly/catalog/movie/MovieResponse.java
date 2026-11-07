package io.github.sfakaly.catalog.movie;

import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record MovieResponse (
        Long id,
        String title,
        String description,
        AgeRating ageRating,
        Integer durationSeconds
) {}
