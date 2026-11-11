package io.github.sfakaly.catalog.cinema.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CinemaCreateRequest(

        @NotBlank(message = "Name shouldn't be empty")
        @Size(max = 120, message = "Name shouldn't be more than 120 symbols")
        String name,

        @NotBlank(message = "Address shouldn't be empty")
        @Size(max = 255, message = "Address shouldn't be more than 255 symbols")
        String address
) {}
