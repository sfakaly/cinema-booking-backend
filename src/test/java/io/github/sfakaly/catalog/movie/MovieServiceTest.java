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
import org.springframework.data.domain.*;

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
    void findById_whenMovieExists_returnsMovieResponse() {
        Long id = 100L;

        Movie movie = movie(id);
        MovieResponse expectedMovieResponse = movieResponse(id, "test title");

        given(movieRepository.findById(id)).willReturn(Optional.of(movie));
        given(movieMapper.toResponse(movie)).willReturn(expectedMovieResponse);

        MovieResponse actualResponse = movieService.findById(id);

        assertEquals(expectedMovieResponse, actualResponse);

        then(movieRepository).should().findById(id);
        then(movieMapper).should().toResponse(movie);
    }

    @Test
    void findById_whenMovieDoesNotExist_throwsResourceNotFoundException() {
        Long id = 999L;

        given(movieRepository.findById(id)).willReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () ->
                movieService.findById(id));

        assertEquals("Movie with %d id not found".formatted(id), ex.getMessage());

        then(movieRepository).should().findById(id);
        then(movieMapper).shouldHaveNoInteractions();
    }

    @Test
    void findAll_returnsMovieResponsePage() {

        Pageable pageable = PageRequest.of(0, 20, Sort.by("id").ascending());
        Movie movie = movie(100L);
        MovieResponse expectedResponse = movieResponse(100L, "test title");

        Page<Movie> moviePage = new PageImpl<>(List.of(movie), pageable, 1);
        Page<MovieResponse> expectedPage = new PageImpl<>(List.of(expectedResponse), pageable, 1);

        given(movieRepository.findAll(pageable)).willReturn(moviePage);
        given(movieMapper.toResponse(movie)).willReturn(expectedResponse);

        Page<MovieResponse> actualPage = movieService.findAll(pageable);

        assertNotNull(actualPage);
        assertEquals(expectedPage, actualPage);

        then(movieRepository).should().findAll(pageable);
        then(movieMapper).should().toResponse(movie);
    }

    @Test
    void create_validDto_returnsMovieResponse() {

        MovieCreateRequest dto = new MovieCreateRequest("test title", "test description", AgeRating.ANY, 1000);
        Movie movie = movie(null);
        Movie savedMovie = movie(100L);
        MovieResponse expectedMovieResponse = movieResponse(100L, "test title");


        given(movieMapper.toEntity(dto)).willReturn(movie);
        given(movieRepository.save(movie)).willReturn(savedMovie);
        given(movieMapper.toResponse(savedMovie)).willReturn(expectedMovieResponse);

        MovieResponse actualResponse = movieService.create(dto);

        assertEquals(expectedMovieResponse, actualResponse);

        then(movieMapper).should().toEntity(dto);
        then(movieRepository).should().save(movie);
        then(movieMapper).should().toResponse(savedMovie);
    }

    @Test
    void delete_whenMovieExists_deletesMovie() {
        Long id = 100L;
        Movie movie = movie(id);

        given(movieRepository.findById(id)).willReturn(Optional.of(movie));

        movieService.delete(id);

        then(movieRepository).should().findById(id);
        then(movieRepository).should().delete(movie);
    }

    @Test
    void delete_whenMovieDoesNotExist_throwsResourceNotFoundException() {
        Long id = 999L;

        given(movieRepository.findById(id)).willReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () ->
                movieService.delete(id));

        assertEquals("Movie with %d id not found".formatted(id), ex.getMessage());

        then(movieRepository).should().findById(id);
        then(movieRepository).should(never()).delete(any(Movie.class));
    }

    @Test
    void patch_whenMovieExists_returnsMovieResponse() {
        Long id = 100L;

        MoviePatchRequest dto = new MoviePatchRequest("changed test title", "test description", AgeRating.ANY, 1000);
        Movie movie = movie(id);
        MovieResponse expectedMovieResponse = movieResponse(id, "changed test title");

        given(movieRepository.findById(id)).willReturn(Optional.of(movie));
        given(movieMapper.toResponse(movie)).willReturn(expectedMovieResponse);

        MovieResponse actualResponse = movieService.patch(id, dto);

        assertEquals(expectedMovieResponse, actualResponse);

        then(movieRepository).should().findById(id);
        then(movieMapper).should().updateEntityFromRequest(dto, movie);
        then(movieMapper).should().toResponse(movie);
    }

    @Test
    void patch_whenMovieDoesNotExist_throwsResourceNotFoundException() {
        Long id = 999L;

        MoviePatchRequest dto = new MoviePatchRequest("changed test title", "test description", AgeRating.ANY, 1000);

        given(movieRepository.findById(id)).willReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () ->
                movieService.patch(id, dto));

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
