package io.github.sfakaly.catalog.seat.type;

import io.github.sfakaly.catalog.seat.type.dto.SeatTypeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/seat-types")
@RequiredArgsConstructor
public class SeatTypeController {

    private final SeatTypeService seatTypeService;

    @GetMapping
    public List<SeatTypeResponse> findAll() {
        return seatTypeService.findAll();
    }
}
