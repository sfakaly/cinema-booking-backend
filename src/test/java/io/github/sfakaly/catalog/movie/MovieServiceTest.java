package io.github.sfakaly.catalog.movie;

import io.github.sfakaly.catalog.movie.dto.MovieCreateRequest;
import io.github.sfakaly.catalog.movie.dto.MoviePatchRequest;
import io.github.sfakaly.catalog.movie.dto.MovieResponse;
import io.github.sfakaly.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MovieServiceTest {

    @Mock
    private MovieRepository movieRepository;

    @Mock
    private MovieMapper movieMapper;

    @InjectMocks
    private MovieService movieService;

    @Test
    void getMovieById_whenMovieExists_returnsMovieResponse() {
        Long id = 100L;

        Movie movie = movie(id);
        MovieResponse expectedMovieResponse = movieResponse(id, "test title");

        given(movieRepository.findById(id)).willReturn(Optional.of(movie));
        given(movieMapper.toResponse(movie)).willReturn(expectedMovieResponse);

        MovieResponse actualResponse = movieService.getMovieById(id);

        assertEquals(expectedMovieResponse, actualResponse);

        then(movieRepository).should().findById(id);
        then(movieMapper).should().toResponse(movie);
    }

    @Test
    void getMovieById_whenMovieDoesNotExist_throwsResourceNotFoundException() {
        Long id = 999L;

        given(movieRepository.findById(id)).willReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () ->
                movieService.getMovieById(id));

        assertEquals("Movie with %d id not found".formatted(id), ex.getMessage());

        then(movieRepository).should().findById(id);
        then(movieMapper).shouldHaveNoInteractions();
    }

    @Test
    void getAllMovies_returnsMovieResponseList() {

        Movie movie = movie(100L);
        MovieResponse expectedMovieResponse = movieResponse(100L, "test title");

        List<Movie> movies = List.of(movie);
        List<MovieResponse> expectedResponseList = List.of(expectedMovieResponse);

        given(movieRepository.findAll()).willReturn(movies);
        given(movieMapper.toResponseList(movies)).willReturn(expectedResponseList);

        List<MovieResponse> actualResponseList = movieService.getAllMovies();

        assertEquals(expectedResponseList, actualResponseList);

        then(movieRepository).should().findAll();
        then(movieMapper).should().toResponseList(movies);
    }

    @Test
    void createMovie_validDto_returnsMovieResponse() {

        MovieCreateRequest dto = new MovieCreateRequest("test title", "test description", AgeRating.ANY, 1000);
        Movie movie = movie(null);
        Movie savedMovie = movie(100L);
        MovieResponse expectedMovieResponse = movieResponse(100L, "test title");


        given(movieMapper.toEntity(dto)).willReturn(movie);
        given(movieRepository.save(movie)).willReturn(savedMovie);
        given(movieMapper.toResponse(savedMovie)).willReturn(expectedMovieResponse);

        MovieResponse actualResponse = movieService.createMovie(dto);

        assertEquals(expectedMovieResponse, actualResponse);

        then(movieMapper).should().toEntity(dto);
        then(movieRepository).should().save(movie);
        then(movieMapper).should().toResponse(savedMovie);
    }

    @Test
    void deleteMovie_whenMovieExists_deletesMovie() {
        Long id = 100L;
        Movie movie = movie(id);

        given(movieRepository.findById(id)).willReturn(Optional.of(movie));

        movieService.deleteMovie(id);

        then(movieRepository).should().findById(id);
        then(movieRepository).should().delete(movie);
    }

    @Test
    void deleteMovie_whenMovieDoesNotExist_throwsResourceNotFoundException() {
        Long id = 999L;

        given(movieRepository.findById(id)).willReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () ->
                movieService.deleteMovie(id));

        assertEquals("Movie with %d id not found".formatted(id), ex.getMessage());

        then(movieRepository).should().findById(id);
        then(movieRepository).should(never()).delete(any(Movie.class));
    }

    @Test
    void updateMovie_whenMovieExists_returnsMovieResponse() {
        Long id = 100L;

        MoviePatchRequest dto = new MoviePatchRequest("changed test title", "test description", AgeRating.ANY, 1000);
        Movie movie = movie(id);
        MovieResponse expectedMovieResponse = movieResponse(id, "changed test title");

        given(movieRepository.findById(id)).willReturn(Optional.of(movie));
        given(movieMapper.toResponse(movie)).willReturn(expectedMovieResponse);

        MovieResponse actualResponse = movieService.updateMovie(id, dto);

        assertEquals(expectedMovieResponse, actualResponse);

        then(movieRepository).should().findById(id);
        then(movieMapper).should().updateEntityFromRequest(dto, movie);
        then(movieMapper).should().toResponse(movie);
    }

    @Test
    void updateMovie_whenMovieDoesNotExist_throwsResourceNotFoundException() {
        Long id = 999L;

        MoviePatchRequest dto = new MoviePatchRequest("changed test title", "test description", AgeRating.ANY, 1000);

        given(movieRepository.findById(id)).willReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () ->
                movieService.updateMovie(id, dto));

        assertEquals("Movie with %d id not found".formatted(id), ex.getMessage());

        then(movieRepository).should().findById(id);
        then(movieMapper).shouldHaveNoInteractions();
    }

    private static Movie movie(Long id) {
        return new Movie(id, "test title", "test description", AgeRating.ANY, 1000);
    }

    private static MovieResponse movieResponse(Long id, String title) {
        return new MovieResponse(id, title, "test description", AgeRating.ANY, 1000);
    }
}
