package io.github.sfakaly.catalog.hall.type;

import io.github.sfakaly.catalog.hall.type.dto.HallTypeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/hall-types")
@RequiredArgsConstructor
public class HallTypeController {

    private final HallTypeService hallTypeService;

    @GetMapping
    public List<HallTypeResponse> findAll() {
        return hallTypeService.findAll();
    }
}
