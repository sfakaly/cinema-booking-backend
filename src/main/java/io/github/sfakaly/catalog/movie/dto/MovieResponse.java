package io.github.sfakaly.catalog.movie.dto;

import io.github.sfakaly.catalog.movie.AgeRating;

public record MovieResponse (
        Long id,
        String title,
        String description,
        AgeRating ageRating,
        Integer durationSeconds
) {}
