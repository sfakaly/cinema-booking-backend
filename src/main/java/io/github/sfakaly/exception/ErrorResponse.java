package io.github.sfakaly.exception;

import java.time.OffsetDateTime;

public record ErrorResponse(
        Integer status,
        String error,
        String message,
        OffsetDateTime timestamp
) {}
