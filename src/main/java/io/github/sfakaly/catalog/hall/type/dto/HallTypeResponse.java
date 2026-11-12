package io.github.sfakaly.catalog.hall.type.dto;

public record HallTypeResponse(
        Integer id,
        String name,
        Integer minimumIntervalSeconds
) {}
