package br.com.alura.challenges.forum.hub.controllers;

import br.com.alura.challenges.forum.hub.exceptions.NotFoundException;
import br.com.alura.challenges.forum.hub.exceptions.UnauthorizedRequisitionException;
import br.com.alura.challenges.forum.hub.models.enums.TopicStatus;
import br.com.alura.challenges.forum.hub.models.requests.UpdateTopicRequest;
import br.com.alura.challenges.forum.hub.models.responses.UpdateTopicResponse;
import br.com.alura.challenges.forum.hub.services.UpdateByIdTopicService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
public class TopicControllerUpdateByIdTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UpdateByIdTopicService service;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() throws Exception {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        JacksonTester.initFields(this, objectMapper);
    }

    @Test
    @WithMockUser
    @DisplayName("Update By Id Given Topic Found By Id And Updated Should Return 200 Ok")
    void updateById_givenTopicFoundByIdAndUpdated_shouldReturn200Ok() throws Exception {
        final var id = 1L;
        final var creationDate = LocalDateTime.of(2024, 12, 31, 12, 30, 22);
        final var request = buildUpdateTopicRequest();
        final var response = buildUpdateTopicResponse(id, creationDate);

        when(service.execute(eq(id), eq(request), any(Authentication.class)))
                .thenReturn(response);

        mockMvc.perform(put("/topicos/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(response.title()))
                .andExpect(jsonPath("$.message").value(response.message()))
                .andExpect(jsonPath("$.status").value(response.status()));

        verify(service, times(1))
                .execute(any(Long.class), any(UpdateTopicRequest.class), any(Authentication.class));
    }

    @Test
    @WithMockUser
    @DisplayName("Update By Id Given Is Not Owner By Topic Should Return 403 Unauthorized")
    void updateById_givenIsNotOwnerByTopic_shouldReturn403Unauthorized() throws Exception {
        final var exceptionMessage = "Não permitido que uma pessoa que não seja o autor modifique o tópico.";
        final var id = 1L;
        final var request = buildUpdateTopicRequest();
        final var status = HttpStatus.UNAUTHORIZED;

        when(service.execute(eq(id), eq(request), any(Authentication.class)))
                .thenThrow(new UnauthorizedRequisitionException(exceptionMessage));

        mockMvc.perform(put("/topicos/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.statusCode").value(status.value()))
                .andExpect(jsonPath("$.status").value(status.name()))
                .andExpect(jsonPath("$.errorMessage").value(exceptionMessage));

        verify(service, times(1))
                .execute(any(Long.class), any(UpdateTopicRequest.class), any(Authentication.class));
    }

    @Test
    @WithMockUser
    @DisplayName("Update By Id Given Topic Not Found By Id Should Return 404 Not Found")
    void updateById_givenTopicNotFoundById_shouldReturn404NotFound() throws Exception {
        final var exceptionMessage = "Não foi encontrado o tópico com o id especificado.";
        final var id = 1000L;
        final var request = buildUpdateTopicRequest();

        when(service.execute(eq(id), eq(request), any(Authentication.class)))
                .thenThrow(new NotFoundException(exceptionMessage));

        mockMvc.perform(put("/topicos/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.statusCode").value(404))
                .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.name()))
                .andExpect(jsonPath("$.errorMessage").value(exceptionMessage));

        verify(service, times(1))
                .execute(any(Long.class), any(UpdateTopicRequest.class), any(Authentication.class));
    }

    UpdateTopicRequest buildUpdateTopicRequest() {
        return new UpdateTopicRequest(
                "Exemplo de Tópico",
                "Esse é um tópico de exemplo",
                TopicStatus.OPENED.getDescription(),
                1L,
                1L
        );
    }

    UpdateTopicResponse buildUpdateTopicResponse(final Long id, final LocalDateTime creationDate) {
        return new UpdateTopicResponse(
                id,
                "Exemplo de Tópico",
                "Esse é um tópico de exemplo",
                creationDate,
                TopicStatus.OPENED.getDescription(),
                1L,
                1L
        );
    }

}
