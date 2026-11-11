package io.github.sfakaly.catalog.movie;

import io.github.sfakaly.catalog.movie.dto.MovieCreateRequest;
import io.github.sfakaly.catalog.movie.dto.MoviePatchRequest;
import io.github.sfakaly.catalog.movie.dto.MovieResponse;
import io.github.sfakaly.exception.ResourceNotFoundException;
import lombok.NonNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MovieController.class)
@AutoConfigureJsonTesters
class MovieControllerTest {

    private static final String BASE_URL = "/api/v1/movies";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JacksonTester<@NonNull MovieCreateRequest> movieCreateRequestJson;

    @Autowired
    private JacksonTester<@NonNull MoviePatchRequest> moviePatchRequestJson;

    @MockitoBean
    private MovieService movieService;

    @Test
    void getMovieById_whenMovieExists_returnsMovie() throws Exception {
        Long id = 100L;
        MovieResponse expectedResponse = movieResponse(id, "test title");

        given(movieService.getMovieById(id)).willReturn(expectedResponse);

        mockMvc.perform(get(BASE_URL + "/{id}", id).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(100))
                .andExpect(jsonPath("$.title").value("test title"))
                .andExpect(jsonPath("$.description").value("test description"))
                .andExpect(jsonPath("$.ageRating").value(AgeRating.ANY.name()))
                .andExpect(jsonPath("$.durationSeconds").value(1000));

        then(movieService).should().getMovieById(id);
    }

    @Test
    void getMovieById_whenMovieDoesNotExist_returnsNotFound() throws Exception {
        Long id = 999L;

        given(movieService.getMovieById(id)).willThrow(new ResourceNotFoundException("Movie with %d id not found".formatted(id)));

        mockMvc.perform(get(BASE_URL + "/{id}", id).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Movie with 999 id not found"))
                .andExpect(jsonPath("$.timestamp").value(notNullValue()));

        then(movieService).should().getMovieById(id);
    }

    @Test
    void getAllMovies_returnsMovieList() throws Exception {
        List<MovieResponse> movies = List.of(movieResponse(100L, "test 1"), movieResponse(101L, "test 2"));

        given(movieService.getAllMovies()).willReturn(movies);

        mockMvc.perform(get(BASE_URL).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(100))
                .andExpect(jsonPath("$[0].title").value("test 1"))
                .andExpect(jsonPath("$[1].id").value(101))
                .andExpect(jsonPath("$[1].title").value("test 2"));

        then(movieService).should().getAllMovies();
    }

    @Test
    void createMovie_validDto_returnsMovie() throws Exception {
        MovieCreateRequest dto = new MovieCreateRequest("test title", "test description", AgeRating.ANY, 1000);
        MovieResponse expectedResponse = movieResponse(100L, "test title");

        given(movieService.createMovie(dto)).willReturn(expectedResponse);

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(movieCreateRequestJson.write(dto).getJson())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(100))
                .andExpect(jsonPath("$.title").value("test title"))
                .andExpect(jsonPath("$.description").value("test description"))
                .andExpect(jsonPath("$.ageRating").value(AgeRating.ANY.name()))
                .andExpect(jsonPath("$.durationSeconds").value(1000));

        then(movieService).should().createMovie(dto);
    }

    @Test
    void deleteMovie_whenMovieExists_deletesMovie() throws Exception {
        Long id = 100L;

        mockMvc.perform(delete(BASE_URL + "/{id}", id).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        then(movieService).should().deleteMovie(id);
    }

    @Test
    void deleteMovie_whenMovieDoesNotExist_returnsNotFound() throws Exception {
        Long id = 999L;

        willThrow(new ResourceNotFoundException("Movie with %d id not found".formatted(id))).given(movieService).deleteMovie(id);

        mockMvc.perform(delete(BASE_URL + "/{id}", id).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Movie with 999 id not found"))
                .andExpect(jsonPath("$.timestamp").value(notNullValue()));

        then(movieService).should().deleteMovie(id);
    }

    @Test
    void updateMovie_whenMovieExists_returnsMovieResponse() throws Exception {
        Long id = 100L;
        MoviePatchRequest dto = new MoviePatchRequest("test updated title", "test description", AgeRating.ANY, 1000);
        MovieResponse expectedResponse = movieResponse(id, "test updated title");

        given(movieService.updateMovie(id, dto)).willReturn(expectedResponse);

        mockMvc.perform(patch(BASE_URL + "/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(moviePatchRequestJson.write(dto).getJson())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(100))
                .andExpect(jsonPath("$.title").value("test updated title"))
                .andExpect(jsonPath("$.description").value("test description"))
                .andExpect(jsonPath("$.ageRating").value(AgeRating.ANY.name()))
                .andExpect(jsonPath("$.durationSeconds").value(1000));

        then(movieService).should().updateMovie(id, dto);
    }

    @Test
    void updateMovie_whenMovieDoesNotExist_throwsResourceNotFoundException() throws Exception {
        Long id = 999L;
        MoviePatchRequest dto = new MoviePatchRequest("test updated title", "test description", AgeRating.ANY, 1000);

        given(movieService.updateMovie(id, dto)).willThrow(new ResourceNotFoundException("Movie with %d id not found".formatted(id)));

        mockMvc.perform(patch(BASE_URL + "/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(moviePatchRequestJson.write(dto).getJson())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Movie with 999 id not found"))
                .andExpect(jsonPath("$.timestamp").value(notNullValue()));

        then(movieService).should().updateMovie(id, dto);
    }

    private static MovieResponse movieResponse(Long id, String title) {
        return new MovieResponse(id, title, "test description", AgeRating.ANY, 1000);
    }
}
