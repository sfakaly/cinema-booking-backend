package io.github.sfakaly.catalog.movie;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record MoviePatchRequest (

        @Size(max = 255, message = "Movie title shouldn't be more than 255 symbols")
        String title,

        String description,

        AgeRating ageRating,

        @Positive(message = "Duration seconds should be more than 0")
        Integer durationSeconds
) {}
