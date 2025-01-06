package br.com.alura.challenges.forum.hub.controllers;

import br.com.alura.challenges.forum.hub.exceptions.NotFoundException;
import br.com.alura.challenges.forum.hub.models.enums.TopicStatus;
import br.com.alura.challenges.forum.hub.models.responses.FindTopicResponse;
import br.com.alura.challenges.forum.hub.models.responses.SimpleCourseResponse;
import br.com.alura.challenges.forum.hub.models.responses.SimpleUserResponse;
import br.com.alura.challenges.forum.hub.services.FindByIdTopicService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
public class TopicControllerFindByIdTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private FindByIdTopicService service;

    @Test
    @WithMockUser
    @DisplayName("Find By Id Given Exists By Id Should Return 200 Ok")
    void findById_givenExistsById_shouldReturn200Ok() throws Exception {
        final var expectedResponse = buildSingleTopic();
        final var existId = 1L;

        when(service.execute(existId)).thenReturn(expectedResponse);

        mockMvc.perform(get("/topicos/{existId}", existId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(expectedResponse.title()))
                .andExpect(jsonPath("$.message").value(expectedResponse.message()))
                .andExpect(jsonPath("$.creationDate").value(expectedResponse.creationDate().toString()))
                .andExpect(jsonPath("$.status").value(expectedResponse.status()))
                .andExpect(jsonPath("$.author.id").value(expectedResponse.author().id()))
                .andExpect(jsonPath("$.author.name").value(expectedResponse.author().name()))
                .andExpect(jsonPath("$.course.id").value(expectedResponse.course().id()))
                .andExpect(jsonPath("$.course.name").value(expectedResponse.course().name()));

        verify(service, times(1)).execute(any());
    }

    @Test
    @WithMockUser
    @DisplayName("Find By Id Given No Exists By Id Should Return 404 Not Found")
    void findById_givenNoExistsById_shouldReturn404NotFound() throws Exception {
        final var exceptionMessage = "Não foi possivel encontrar o tópico pelo id.";
        final var expectedId = 10000L;

        when(service.execute(expectedId)).thenThrow(new NotFoundException(exceptionMessage));

        mockMvc.perform(get("/topicos/{expectedId}", expectedId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.statusCode").value(404))
                .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.name()))
                .andExpect(jsonPath("$.errorMessage").value(exceptionMessage));

        verify(service, times(1)).execute(any());
    }

    private FindTopicResponse buildSingleTopic() {
        return new FindTopicResponse(
                "Spring JPA",
                "Como construir query methods",
                LocalDateTime.of(2024, 12, 25, 17, 30, 22),
                TopicStatus.OPENED.getDescription(),
                new SimpleUserResponse(1L, "Fulano"),
                new SimpleCourseResponse(1L, "Curso Spring JPA")
        );
    }

}
