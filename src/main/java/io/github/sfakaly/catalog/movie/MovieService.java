package io.github.sfakaly.catalog.movie;

import io.github.sfakaly.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MovieService {

    private final MovieRepository movieRepository;
    private final MovieMapper movieMapper;

    @Transactional(readOnly = true)
    public MovieResponse getMovieById(Long id) {
        return movieRepository.findById(id)
                .map(movieMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Movie with %d id not found".formatted(id)));
    }

    @Transactional(readOnly = true)
    public List<MovieResponse> getAllMovies() {
        List<Movie> movies = movieRepository.findAll();
        return movieMapper.toResponseList(movies);
    }

    @Transactional
    public MovieResponse createMovie(MovieCreateRequest dto) {
        Movie movie = movieMapper.toEntity(dto);
        Movie savedMovie = movieRepository.save(movie);
        return movieMapper.toResponse(savedMovie);
    }

    @Transactional
    public void deleteMovie(Long id) {
        if (!movieRepository.existsById(id)) throw new ResourceNotFoundException("Movie with %d id not found".formatted(id));
        movieRepository.deleteById(id);
    }

    @Transactional
    public MovieResponse updateMovie(Long id, MovieCreateRequest dto) {
        Movie existingMovie = movieRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movie with %d id not found".formatted(id)));

        movieMapper.updateEntityFromRequest(dto, existingMovie);
        Movie updatedMovie = movieRepository.save(existingMovie);
        return movieMapper.toResponse(updatedMovie);
    }
}
