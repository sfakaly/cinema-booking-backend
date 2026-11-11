package io.github.sfakaly.catalog.movie;

import io.github.sfakaly.catalog.movie.dto.MovieCreateRequest;
import io.github.sfakaly.catalog.movie.dto.MoviePatchRequest;
import io.github.sfakaly.catalog.movie.dto.MovieResponse;
import io.github.sfakaly.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MovieService {

    private final MovieRepository movieRepository;
    private final MovieMapper movieMapper;

    @Transactional(readOnly = true)
    public MovieResponse findById(Long id) {
        return movieRepository.findById(id)
                .map(movieMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Movie with %d id not found".formatted(id)));
    }

    @Transactional(readOnly = true)
    public Page<MovieResponse> findAll(Pageable pageable) {
        Page<Movie> moviePage = movieRepository.findAll(pageable);
        return moviePage.map(movieMapper::toResponse);
    }

    @Transactional
    public MovieResponse create(MovieCreateRequest dto) {
        Movie movie = movieMapper.toEntity(dto);
        Movie savedMovie = movieRepository.save(movie);
        return movieMapper.toResponse(savedMovie);
    }

    @Transactional
    public void delete(Long id) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movie with %d id not found".formatted(id)));

        movieRepository.delete(movie);
    }

    @Transactional
    public MovieResponse patch(Long id, MoviePatchRequest dto) {
        Movie existingMovie = movieRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movie with %d id not found".formatted(id)));

        movieMapper.updateEntityFromRequest(dto, existingMovie);
        return movieMapper.toResponse(existingMovie);
    }
}
