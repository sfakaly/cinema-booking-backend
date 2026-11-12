package io.github.sfakaly.catalog.seat.type;

import io.github.sfakaly.catalog.seat.type.dto.SeatTypeResponse;
import io.github.sfakaly.config.CentralMappingConfig;
import org.mapstruct.Mapper;

@Mapper(config = CentralMappingConfig.class)
public interface SeatTypeMapper {

    SeatTypeResponse toResponse(SeatType seatType);
}
