package br.com.alura.challenges.forum.hub.controllers;

import br.com.alura.challenges.forum.hub.exceptions.NotFoundException;
import br.com.alura.challenges.forum.hub.exceptions.UnauthorizedRequisitionException;
import br.com.alura.challenges.forum.hub.services.DeleteByIdTopicService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
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
    private DeleteByIdTopicService service;

    @Test
    @WithMockUser(username = "fulana.user@gmail.com", roles = "USER")
    @DisplayName("Delete By Id Given Logged User Is Topic Owner Should Return 204 No Content")
    void deleteById_givenLoggedUserIsTopicOwner_shouldReturn204NoContent() throws Exception {
        final var id = 1L;

        doNothing().when(service).execute(eq(id), any(Authentication.class));

        mockMvc.perform(delete("/topicos/{id}", id))
                .andExpect(status().isNoContent());

        verify(service, times(1)).execute(any(Long.class), any(Authentication.class));
    }

    @Test
    @WithMockUser(username = "fulana.admin@gmail.com", roles = "ADMIN")
    @DisplayName("Delete By Id Given Logged User Is Admin And Not Topic Owner Should Return 204 No Content")
    void deleteById_givenLoggedUserIsAdminAndNotTopicOwner_shouldReturn204NoContent() throws Exception {
        final var id = 1L;

        doNothing().when(service).execute(eq(id), any(Authentication.class));

        mockMvc.perform(delete("/topicos/{id}", id))
                .andExpect(status().isNoContent());

        verify(service, times(1)).execute(any(Long.class), any(Authentication.class));
    }

    @Test
    @WithMockUser
    @DisplayName("Delete By Id Given Logged User Is Not Topic Owner Should Return 403 Forbidden")
    void deleteById_givenLoggedUserIsNotTopicOwner_shouldReturn403Forbidden() throws Exception {
        final var id = 1L;
        final var exceptionMessage = String.format("Não é possível remover o Tópico indicado pelo ID %d.", id);
        final var status = HttpStatus.UNAUTHORIZED;

        doThrow(new UnauthorizedRequisitionException(exceptionMessage))
                .when(service).execute(eq(id), any(Authentication.class));

        mockMvc.perform(delete("/topicos/{id}", id))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.statusCode").value(status.value()))
                .andExpect(jsonPath("$.status").value(status.name()))
                .andExpect(jsonPath("$.errorMessage").value(exceptionMessage));

        verify(service, times(1)).execute(any(Long.class), any(Authentication.class));
    }

    @Test
    @WithMockUser(username = "fulana.user@gmail.com", roles = "USER")
    @DisplayName("Delete By Id Given Not Found Topic By Id Should Return 404 Not Found")
    void deleteById_givenNotFoundTopicById_shouldReturn404NotFound() throws Exception {
        final var exceptionMessage = "Não foi possivel encontrar o tópico especificado.";
        final var id = 1000L;

        doThrow(new NotFoundException(exceptionMessage))
                .when(service).execute(eq(id), any(Authentication.class));

        mockMvc.perform(delete("/topicos/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.statusCode").value(404))
                .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.name()))
                .andExpect(jsonPath("$.errorMessage").value(exceptionMessage));

        verify(service, times(1)).execute(any(Long.class), any(Authentication.class));
    }

}
