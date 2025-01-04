package br.com.alura.challenges.forum.hub.controllers;

import br.com.alura.challenges.forum.hub.exceptions.NotFoundException;
import br.com.alura.challenges.forum.hub.models.enums.TopicStatus;
import br.com.alura.challenges.forum.hub.models.responses.FindTopicResponse;
import br.com.alura.challenges.forum.hub.models.responses.SimpleCourseResponse;
import br.com.alura.challenges.forum.hub.models.responses.SimpleUserResponse;
import br.com.alura.challenges.forum.hub.services.FindAllTopicService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
public class TopicControllerFindAllTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private FindAllTopicService service;

    private LocalDateTime creationDate;

    @BeforeEach
    void setUp() throws Exception {
        creationDate = LocalDateTime.of(2024, 12, 23, 7, 30, 22);
    }

    @Test
    @WithMockUser
    @DisplayName("Find All Given Paged Defined Should Return 200 Ok")
    void findAll_givenPagedDefined_shouldReturn200Ok() throws Exception {
        final Map<String, String> params = Map.of();
        final var pageable = PageRequest.of(0, 10, Sort.by("name"));
        final var expectedResponseFindAll = buildTopicResponse();

        when(service.execute(params, pageable))
                .thenReturn(new PageImpl<>(List.of(expectedResponseFindAll)));

        final var result = mockMvc.perform(get("/topicos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("page", String.valueOf(pageable.getPageNumber()))
                        .param("size", String.valueOf(pageable.getPageSize()))
                        .param("sort", pageable.getSort().stream().toList().get(0).getProperty()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].title").value(expectedResponseFindAll.title()))
                .andExpect(jsonPath("$.content[0].message").value(expectedResponseFindAll.message()))
                .andExpect(jsonPath("$.content[0].creationDate").value(expectedResponseFindAll.creationDate().toString()))
                .andExpect(jsonPath("$.content[0].status").value(expectedResponseFindAll.status()))
                .andExpect(jsonPath("$.content[0].author.id").value(expectedResponseFindAll.author().id()))
                .andExpect(jsonPath("$.content[0].author.name").value(expectedResponseFindAll.author().name()))
                .andExpect(jsonPath("$.content[0].course.id").value(expectedResponseFindAll.course().id()))
                .andExpect(jsonPath("$.content[0].course.name").value(expectedResponseFindAll.course().name()));

        verify(service, times(1)).execute(any(), any());
    }

    @Test
    @WithMockUser
    @DisplayName("Find All When Not Founded Should Return 404 Not Found")
    void findAll_whenNotFounded_shouldReturn404NotFound() throws Exception {
        final var exceptionMessage = "Não foram encontrados tópicos com as especificações indicadas.";
        final Map<String, String> params = Map.of();
        final var pageable = PageRequest.of(0, 10, Sort.by("name"));

        when(service.execute(params, pageable))
                .thenThrow(new NotFoundException(exceptionMessage));

        mockMvc.perform(get("/topicos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("page", String.valueOf(pageable.getPageNumber()))
                        .param("size", String.valueOf(pageable.getPageSize()))
                        .param("sort", pageable.getSort().stream().toList().get(0).getProperty()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.statusCode").value(404))
                .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.name()))
                .andExpect(jsonPath("$.errorMessage").value(exceptionMessage));

        verify(service, times(1)).execute(any(), any());
    }

    @Test
    @WithMockUser
    @DisplayName("Find All Given First 10 Topics By Creation Date Should Return 200 OK")
    void findAll_givenFirst10TopicsByCreationDate_shouldReturn200Ok() throws Exception {
        final Map<String, String> params = Map.of();
        final var pageableDef = PageRequest.of(0, 10, Sort.by("creationDate"));
        final var expectedResponse = buildTopicResponse();

        when(service.execute(params, pageableDef))
                .thenReturn(new PageImpl<>(new ArrayList<>(Collections.nCopies(10, expectedResponse))));

        mockMvc.perform(get("/topicos")
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(10)))
                .andExpect(jsonPath("$.content[0].title").value(expectedResponse.title()))
                .andExpect(jsonPath("$.content[0].message").value(expectedResponse.message()))
                .andExpect(jsonPath("$.content[0].creationDate").value(expectedResponse.creationDate().toString()))
                .andExpect(jsonPath("$.content[0].author.id").value(expectedResponse.author().id()))
                .andExpect(jsonPath("$.content[0].author.name").value(expectedResponse.author().name()))
                .andExpect(jsonPath("$.content[0].course.id").value(expectedResponse.course().id()))
                .andExpect(jsonPath("$.content[0].course.name").value(expectedResponse.course().name()));

        verify(service, times(1)).execute(any(), any());
    }

    @Test
    @WithMockUser
    @DisplayName("Find All Given First 10 Topics By Creation Date Not Founded Should Return 404 Not Found")
    void findAll_givenFirst10TopicsByCreationDateNotFounded_shouldReturn404NotFound() throws Exception {
        final Map<String, String> params = Map.of();
        final var pageableDef = PageRequest.of(0, 10, Sort.by("creationDate"));
        final var exceptionMessage = "Não foram encontrados tópicos com as especificações indicadas.";

        when(service.execute(params, pageableDef))
                .thenThrow(new NotFoundException(exceptionMessage));

        mockMvc.perform(get("/topicos")
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.statusCode").value(404))
                .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.name()))
                .andExpect(jsonPath("$.errorMessage").value(exceptionMessage));

        verify(service, times(1)).execute(any(), any());
    }

    @Test
    @WithMockUser
    @DisplayName("Find All Given Params Has Course Name Should Return 200 Ok")
    void findAll_givenParamsHasCourseName_shouldReturn200Ok() throws Exception {
        final var expectedCourseName = "Curso Spring JPA";
        final var expectedTarget = "course";
        final Map<String, String> params = Map.of(expectedTarget, expectedCourseName);
        final var pageable = PageRequest.of(0, 10, Sort.by("course"));
        final var expectedResponse = Collections.nCopies(5, buildTopicResponse());

        when(service.execute(params, pageable))
                .thenReturn(new PageImpl<>(expectedResponse));

        mockMvc.perform(get("/topicos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param(expectedTarget, params.get(expectedTarget))
                        .param("page", String.valueOf(pageable.getPageNumber()))
                        .param("size", String.valueOf(pageable.getPageSize()))
                        .param("sort", pageable.getSort().stream().toList().get(0).getProperty()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(5)))
                .andExpect(jsonPath("$.content[0].title").value(expectedResponse.get(0).title()))
                .andExpect(jsonPath("$.content[0].message").value(expectedResponse.get(0).message()))
                .andExpect(jsonPath("$.content[0].creationDate").value(expectedResponse.get(0).creationDate().toString()))
                .andExpect(jsonPath("$.content[0].author.id").value(expectedResponse.get(0).author().id()))
                .andExpect(jsonPath("$.content[0].author.name").value(expectedResponse.get(0).author().name()))
                .andExpect(jsonPath("$.content[0].course.id").value(expectedResponse.get(0).course().id()))
                .andExpect(jsonPath("$.content[0].course.name").value(expectedCourseName));

        verify(service, times(1)).execute(any(), any());
    }

    @Test
    @WithMockUser
    @DisplayName("Find All Given Params Has Course Name Not Founded Should Return 404 Not Found")
    void findAll_givenParamsHasCourseNameNotFounded_shouldReturn404NotFound() throws Exception {
        final var exceptionMessage = "Não foram encontrados tópicos com as especificações indicadas.";
        final var expectedCourseName = "Curso Spring JPA";
        final var expectedTarget = "course";
        final Map<String, String> params = Map.of(expectedTarget, expectedCourseName);
        final var pageable = PageRequest.of(0, 10, Sort.by("course"));

        when(service.execute(params, pageable))
                .thenThrow(new NotFoundException(exceptionMessage));

        mockMvc.perform(get("/topicos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param(expectedTarget, params.get(expectedTarget))
                        .param("page", String.valueOf(pageable.getPageNumber()))
                        .param("size", String.valueOf(pageable.getPageSize()))
                        .param("sort", pageable.getSort().stream().toList().get(0).getProperty()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.statusCode").value(404))
                .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.name()))
                .andExpect(jsonPath("$.errorMessage").value(exceptionMessage));

        verify(service, times(1)).execute(any(), any());
    }

    @Test
    @WithMockUser
    @DisplayName("Find All Given Params Has Creation Year Should Return 200 Ok")
    void findAll_givenParamsHasCreationYear_shouldReturn200Ok() throws Exception {
        final var expectedTarget = "creation-year";
        final var expectedYear = "2024";
        final var params = Map.of(expectedTarget, expectedYear);
        final var pageable = PageRequest.of(0, 10, Sort.by("creationDate"));

        final var expectedResponse = Collections.nCopies(5, buildTopicResponse());

        when(service.execute(params, pageable))
                .thenReturn(new PageImpl<>(expectedResponse));

        mockMvc.perform(get("/topicos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param(expectedTarget, expectedYear)
                        .param("page", String.valueOf(pageable.getPageNumber()))
                        .param("size", String.valueOf(pageable.getPageSize()))
                        .param("sort", pageable.getSort().stream().toList().get(0).getProperty()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(5)))
                .andExpect(jsonPath("$.content[*].creationDate", everyItem(containsString(expectedYear))))
                .andExpect(jsonPath("$.content[0].title").value(expectedResponse.get(0).title()))
                .andExpect(jsonPath("$.content[0].message").value(expectedResponse.get(0).message()))
                .andExpect(jsonPath("$.content[0].creationDate").value(expectedResponse.get(0).creationDate().toString()))
                .andExpect(jsonPath("$.content[0].author.id").value(expectedResponse.get(0).author().id()))
                .andExpect(jsonPath("$.content[0].author.name").value(expectedResponse.get(0).author().name()))
                .andExpect(jsonPath("$.content[0].course.id").value(expectedResponse.get(0).course().id()))
                .andExpect(jsonPath("$.content[0].course.name").value(expectedResponse.get(0).course().name()));

        verify(service, times(1)).execute(any(), any());
    }

    @Test
    @WithMockUser
    @DisplayName("Find All Given Params Has Creation Year Not Founded Should Return 404 Not Found")
    void findAll_givenParamsHasCreationYearNotFounded_shouldReturn404NotFound() throws Exception {
        final var exceptionMessage = "Não foram encontrados tópicos com as especificações indicadas.";
        final var expectedTarget = "creation-year";
        final var expectedYear = "2024";
        final var params = Map.of(expectedTarget, expectedYear);
        final var pageable = PageRequest.of(0, 10, Sort.by("creationDate"));

        when(service.execute(params, pageable))
                .thenThrow(new NotFoundException(exceptionMessage));

        mockMvc.perform(get("/topicos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param(expectedTarget, expectedYear)
                        .param("page", String.valueOf(pageable.getPageNumber()))
                        .param("size", String.valueOf(pageable.getPageSize()))
                        .param("sort", pageable.getSort().stream().toList().get(0).getProperty()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.statusCode").value(404))
                .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.name()))
                .andExpect(jsonPath("$.errorMessage").value(exceptionMessage));

        verify(service, times(1)).execute(any(), any());
    }

    FindTopicResponse buildTopicResponse() {
        return new FindTopicResponse(
                "Titulo do tópico",
                "Mensagem do tópico",
                creationDate,
                TopicStatus.OPENED.getDescription(),
                new SimpleUserResponse(1L, "Fulano"),
                new SimpleCourseResponse(1L, "Curso Spring JPA")
        );
    }

}
