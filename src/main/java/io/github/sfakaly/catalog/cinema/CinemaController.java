package io.github.sfakaly.catalog.cinema;

import io.github.sfakaly.catalog.cinema.dto.CinemaCreateRequest;
import io.github.sfakaly.catalog.cinema.dto.CinemaPatchRequest;
import io.github.sfakaly.catalog.cinema.dto.CinemaResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cinemas")
@RequiredArgsConstructor
public class CinemaController {

    private final CinemaService cinemaService;

    @GetMapping("/{id}")
    public CinemaResponse findById(@PathVariable Integer id) {
        return cinemaService.findById(id);
    }

    @GetMapping
    public Page<CinemaResponse> findAll(@ParameterObject @PageableDefault(size = 20, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        return cinemaService.findAll(pageable);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CinemaResponse create(@RequestBody @Valid CinemaCreateRequest request) {
        return cinemaService.create(request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        cinemaService.delete(id);
    }

    @PatchMapping("/{id}")
    public CinemaResponse patch(@PathVariable Integer id, @RequestBody @Valid CinemaPatchRequest request) {
        return cinemaService.patch(id, request);
    }
}
