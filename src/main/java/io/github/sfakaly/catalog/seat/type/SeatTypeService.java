package io.github.sfakaly.catalog.seat.type;

import io.github.sfakaly.catalog.seat.type.dto.SeatTypeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SeatTypeService {

    private final SeatTypeRepository seatTypeRepository;
    private final SeatTypeMapper seatTypeMapper;

    @Transactional(readOnly = true)
    public List<SeatTypeResponse> findAll() {
        return seatTypeRepository.findAll()
                .stream()
                .map(seatTypeMapper::toResponse)
                .toList();
    }
}
