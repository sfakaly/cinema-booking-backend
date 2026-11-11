package io.github.sfakaly.catalog.movie;

import io.github.sfakaly.catalog.movie.dto.MovieCreateRequest;
import io.github.sfakaly.catalog.movie.dto.MoviePatchRequest;
import io.github.sfakaly.catalog.movie.dto.MovieResponse;
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
@RequestMapping("/api/v1/movies")
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;

    @GetMapping("/{id}")
    public MovieResponse getById(@PathVariable Long id) {
        return movieService.findById(id);
    }

    @GetMapping
    public Page<MovieResponse> findAll(@ParameterObject @PageableDefault(size = 20, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        return movieService.findAll(pageable);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MovieResponse create(@RequestBody @Valid MovieCreateRequest request) {
        return movieService.create(request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        movieService.delete(id);
    }

    @PatchMapping("/{id}")
    public MovieResponse patch(@PathVariable Long id, @RequestBody @Valid MoviePatchRequest request) {
        return movieService.patch(id, request);
    }
}
