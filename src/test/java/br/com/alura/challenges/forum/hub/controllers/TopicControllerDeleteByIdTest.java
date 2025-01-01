package br.com.alura.challenges.forum.hub.controllers;

import br.com.alura.challenges.forum.hub.exceptions.NotFoundException;
import br.com.alura.challenges.forum.hub.services.DeleteTopicService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
public class TopicControllerDeleteByIdTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DeleteTopicService service;

    @Test
    @DisplayName("Delete By Id Given Found Topic By Id And Delete Should Return 204 No Content")
    void deleteById_givenFoundTopicByIdAndDelete_shouldReturn204NoContent() throws Exception {
        final var id = 1L;

        doNothing().when(service).execute(id);

        mockMvc.perform(delete("/topicos/{id}", id))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Delete By Id Given Not Found Topic By Id Should Return 404 Not Found")
    void deleteById_givenNotFoundTopicById_shouldReturn404NotFound() throws Exception {
        final var exceptionMessage = "Não foi possivel encontrar o tópico especificado.";
        final var id = 1000L;

        doThrow(new NotFoundException(exceptionMessage))
                .when(service).execute(id);

        mockMvc.perform(delete("/topicos/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.statusCode").value(404))
                .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.name()))
                .andExpect(jsonPath("$.errorMessage").value(exceptionMessage));
    }

}
