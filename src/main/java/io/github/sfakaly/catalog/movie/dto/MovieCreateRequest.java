package io.github.sfakaly.catalog.movie.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.github.sfakaly.catalog.movie.AgeRating;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record MovieCreateRequest (

        @NotBlank(message = "Movie title shouldn't be empty")
        @Size(max = 255, message = "Movie title shouldn't be more than 255 symbols")
        String title,

        String description,

        @NotNull(message = "Age rating shouldn't be empty")
        AgeRating ageRating,

        @NotNull(message = "Duration seconds shouldn't be empty")
        @Positive(message = "Duration seconds should be more than 0")
        Integer durationSeconds
) {}
