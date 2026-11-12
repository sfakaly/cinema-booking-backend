package io.github.sfakaly.catalog.seat.type.dto;

import java.math.BigDecimal;

public record SeatTypeResponse(
        Integer id,
        String name,
        BigDecimal surcharge
) {}
