package io.github.sfakaly.catalog.movie;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

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
