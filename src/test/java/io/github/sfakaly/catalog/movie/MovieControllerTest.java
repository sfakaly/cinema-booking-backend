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
import org.springframework.data.domain.*;
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
    private JacksonTester<MovieCreateRequest> movieCreateRequestJson;

    @Autowired
    private JacksonTester<MoviePatchRequest> moviePatchRequestJson;

    @MockitoBean
    private MovieService movieService;

    @Test
    void findById_whenMovieExists_returnsMovie() throws Exception {
        Long id = 100L;
        MovieResponse expectedResponse = movieResponse(id, "test title");

        given(movieService.findById(id)).willReturn(expectedResponse);

        mockMvc.perform(get(BASE_URL + "/{id}", id).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(100))
                .andExpect(jsonPath("$.title").value("test title"))
                .andExpect(jsonPath("$.description").value("test description"))
                .andExpect(jsonPath("$.ageRating").value(AgeRating.ANY.name()))
                .andExpect(jsonPath("$.durationSeconds").value(1000));

        then(movieService).should().findById(id);
    }

    @Test
    void findById_whenMovieDoesNotExist_returnsNotFound() throws Exception {
        Long id = 999L;

        given(movieService.findById(id)).willThrow(new ResourceNotFoundException("Movie with %d id not found".formatted(id)));

        mockMvc.perform(get(BASE_URL + "/{id}", id).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Movie with 999 id not found"))
                .andExpect(jsonPath("$.timestamp").value(notNullValue()));

        then(movieService).should().findById(id);
    }

    @Test
    void findAll_returnsMovieList() throws Exception {
        List<MovieResponse> content = List.of(
                movieResponse(100L, "test 1"),
                movieResponse(101L, "test 2")
        );
        Pageable pageable = PageRequest.of(0, 20, Sort.by("id").ascending());
        Page<MovieResponse> expectedPage = new PageImpl<>(content, pageable, 2);

        given(movieService.findAll(pageable)).willReturn(expectedPage);

        mockMvc.perform(get(BASE_URL)
                        .param("page", "0")
                        .param("size", "20")
                        .param("sort", "id,asc")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.content[0].id").value(100))
                .andExpect(jsonPath("$.content[0].title").value("test 1"))
                .andExpect(jsonPath("$.content[1].id").value(101))
                .andExpect(jsonPath("$.content[1].title").value("test 2"))
                .andExpect(jsonPath("$.totalElements").value(2))
                .andExpect(jsonPath("$.totalPages").value(1));

        then(movieService).should().findAll(pageable);
    }

    @Test
    void create_validDto_returnsMovie() throws Exception {
        MovieCreateRequest dto = new MovieCreateRequest("test title", "test description", AgeRating.ANY, 1000);
        MovieResponse expectedResponse = movieResponse(100L, "test title");

        given(movieService.create(dto)).willReturn(expectedResponse);

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

        then(movieService).should().create(dto);
    }

    @Test
    void delete_whenMovieExists_deletesMovie() throws Exception {
        Long id = 100L;

        mockMvc.perform(delete(BASE_URL + "/{id}", id).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        then(movieService).should().delete(id);
    }

    @Test
    void delete_whenMovieDoesNotExist_returnsNotFound() throws Exception {
        Long id = 999L;

        willThrow(new ResourceNotFoundException("Movie with %d id not found".formatted(id))).given(movieService).delete(id);

        mockMvc.perform(delete(BASE_URL + "/{id}", id).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Movie with 999 id not found"))
                .andExpect(jsonPath("$.timestamp").value(notNullValue()));

        then(movieService).should().delete(id);
    }

    @Test
    void patch_whenMovieExists_returnsMovieResponse() throws Exception {
        Long id = 100L;
        MoviePatchRequest dto = new MoviePatchRequest("test updated title", "test description", AgeRating.ANY, 1000);
        MovieResponse expectedResponse = movieResponse(id, "test updated title");

        given(movieService.patch(id, dto)).willReturn(expectedResponse);

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

        then(movieService).should().patch(id, dto);
    }

    @Test
    void patch_whenMovieDoesNotExist_throwsResourceNotFoundException() throws Exception {
        Long id = 999L;
        MoviePatchRequest dto = new MoviePatchRequest("test updated title", "test description", AgeRating.ANY, 1000);

        given(movieService.patch(id, dto)).willThrow(new ResourceNotFoundException("Movie with %d id not found".formatted(id)));

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

        then(movieService).should().patch(id, dto);
    }

    private static MovieResponse movieResponse(Long id, String title) {
        return new MovieResponse(id, title, "test description", AgeRating.ANY, 1000);
    }
}
