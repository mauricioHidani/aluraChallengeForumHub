package br.com.alura.challenges.forum.hub.controllers;

import br.com.alura.challenges.forum.hub.models.enums.TopicStatus;
import br.com.alura.challenges.forum.hub.models.requests.RegisterTopicRequest;
import br.com.alura.challenges.forum.hub.models.responses.RegisterTopicResponse;
import br.com.alura.challenges.forum.hub.models.responses.SimpleAuthorResponse;
import br.com.alura.challenges.forum.hub.models.responses.SimpleCourseResponse;
import br.com.alura.challenges.forum.hub.models.responses.ValidationErrorResponse;
import br.com.alura.challenges.forum.hub.services.RegisterTopicService;
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
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
class TopicControllerRegisterTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RegisterTopicService registerTopicService;

    private ObjectMapper objectMapper;
    private JacksonTester<RegisterTopicRequest> json;

    private LocalDateTime creationDate;
    private RegisterTopicRequest request;
    private RegisterTopicResponse response;

    @BeforeEach
    public void setup() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        JacksonTester.initFields(this, objectMapper);

        creationDate = LocalDateTime.now(Clock.systemDefaultZone()).minusMinutes(1);
        request = buildRegisterTopicRequest();
        response = buildRegisterTopicResponse(creationDate);
    }

    @Test
    @WithMockUser
    @DisplayName("Register New Topic Given Successful Should Return 200 Ok")
    void registerNewTopic_givenSuccessful_shouldReturn200Ok() throws Exception {
        when(registerTopicService.execute(any(RegisterTopicRequest.class)))
                .thenReturn(response);

        final var result = mockMvc.perform(
                post("/topicos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json.write(request).getJson())
                )
                .andExpect(status().isCreated())
                .andReturn();

        final var resultJson = result.getResponse().getContentAsString();
        final var contentResponse = objectMapper.readValue(resultJson, RegisterTopicResponse.class);

        assertEquals(contentResponse.title(), request.title());
        assertEquals(contentResponse.message(), request.message());
        assertEquals(contentResponse.author().id(), request.authorId());
        assertEquals(contentResponse.course().id(), request.courseId());
        assertEquals(contentResponse.status(), TopicStatus.OPENED.getDescription());

        assertEquals(contentResponse.creationDate().toLocalDate(), creationDate.toLocalDate());
        assertEquals(contentResponse.creationDate().getHour(), creationDate.getHour());
        assertEquals(contentResponse.creationDate().getMinute(), creationDate.getMinute());
    }

    @Test
    @WithMockUser
    @DisplayName("Register New Topic Given Title Is Null Should Return 400 Bad Request")
    void registerNewTopic_givenTitleIsNull_shouldReturn400BadRequest() throws Exception {
        final var fieldExpected = "title";
        final var messageExpected = "O titulo do tópico é um campo obrigatório.";
        final var requestData = new RegisterTopicRequest(null, "message", 1L, 1L);

        final var result = mockMvc.perform(
                post("/topicos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json.write(requestData).getJson())
                )
                .andExpect(status().isBadRequest())
                .andReturn();

        final var resultJson = result.getResponse().getContentAsString();
        final var contentResponse = objectMapper.readValue(resultJson, ValidationErrorResponse.class);

        assertAll(() -> contentResponse.errors().forEach((key, value) -> {
            assertEquals(key, fieldExpected);
            assertEquals(value, messageExpected);
        }));
    }

    @Test
    @WithMockUser
    @DisplayName("Register New Topic Given Title Is Less than 3 Characters Should Return 400 Bad Request")
    void registerNewTopic_givenTitleIsLessThan3Characters_shouldReturn400BadRequest() throws Exception {
        final var fieldExpected = "title";
        final var messageExpected = "O titulo deve conter entre 3 à 128 caracteres.";

        final var requestData = new RegisterTopicRequest("T", "message", 1L, 1L);

        final var result = mockMvc.perform(
                        post("/topicos")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json.write(requestData).getJson())
                )
                .andExpect(status().isBadRequest())
                .andReturn();

        final var resultJson = result.getResponse().getContentAsString();
        final var contentResponse = objectMapper.readValue(resultJson, ValidationErrorResponse.class);

        assertAll(() -> contentResponse.errors().forEach((key, value) -> {
            assertEquals(key, fieldExpected);
            assertEquals(value, messageExpected);
        }));
    }

    @Test
    @WithMockUser
    @DisplayName("Register New Topic Given Title Is Long Than 128 Characters Should Return 400 Bad Request")
    void registerNewTopic_givenTitleIsLongThan128Characters_shouldReturn400BadRequest() throws Exception {
        final var fieldExpected = "title";
        final var messageExpected = "O titulo deve conter entre 3 à 128 caracteres.";

        var newChars = new char[129];
        Arrays.fill(newChars, 'a');
        final var requestData = new RegisterTopicRequest(new String(newChars), "message", 1L, 1L);

        final var result = mockMvc.perform(
                        post("/topicos")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json.write(requestData).getJson())
                )
                .andExpect(status().isBadRequest())
                .andReturn();

        final var resultJson = result.getResponse().getContentAsString();
        final var contentResponse = objectMapper.readValue(resultJson, ValidationErrorResponse.class);

        assertAll(() -> contentResponse.errors().forEach((key, value) -> {
            assertEquals(key, fieldExpected);
            assertEquals(value, messageExpected);
        }));
    }

    @Test
    @WithMockUser
    @DisplayName("Register New Topic Given Message Is Null Should Return 400 Bad Request")
    void registerNewTopic_givenMessageIsNull_shouldReturn400BadRequest() throws Exception {
        final var fieldExpected = "message";
        final var messageExpected = "A mensagem do tópico é um campo obrigatório.";
        final var requestData = new RegisterTopicRequest("Titulo", null, 1L, 1L);

        final var result = mockMvc.perform(
                        post("/topicos")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json.write(requestData).getJson())
                )
                .andExpect(status().isBadRequest())
                .andReturn();

        final var resultJson = result.getResponse().getContentAsString();
        final var contentResponse = objectMapper.readValue(resultJson, ValidationErrorResponse.class);

        assertAll(() -> contentResponse.errors().forEach((key, value) -> {
            assertEquals(key, fieldExpected);
            assertEquals(value, messageExpected);
        }));
    }

    @Test
    @WithMockUser
    @DisplayName("Register New Topic Given Message Is Less than 3 Characters Should Return 400 Bad Request")
    void registerNewTopic_givenMessageIsLessThan3Characters_shouldReturn400BadRequest() throws Exception {
        final var fieldExpected = "message";
        final var messageExpected = "A mensagem deve conter 3 à 16000 caracteres.";

        final var requestData = new RegisterTopicRequest("Titulo", "m", 1L, 1L);

        final var result = mockMvc.perform(
                        post("/topicos")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json.write(requestData).getJson())
                )
                .andExpect(status().isBadRequest())
                .andReturn();

        final var resultJson = result.getResponse().getContentAsString();
        final var contentResponse = objectMapper.readValue(resultJson, ValidationErrorResponse.class);

        assertAll(() -> contentResponse.errors().forEach((key, value) -> {
            assertEquals(key, fieldExpected);
            assertEquals(value, messageExpected);
        }));
    }

    @Test
    @WithMockUser
    @DisplayName("Register New Topic Given Message Is Long Than 128 Characters Should Return 400 Bad Request")
    void registerNewTopic_givenMessageIsLongThan128Characters_shouldReturn400BadRequest() throws Exception {
        final var fieldExpected = "message";
        final var messageExpected = "A mensagem deve conter 3 à 16000 caracteres.";

        var newChars = new char[16001];
        Arrays.fill(newChars, 'a');
        final var requestData = new RegisterTopicRequest("Titulo", new String(newChars), 1L, 1L);

        final var result = mockMvc.perform(
                        post("/topicos")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json.write(requestData).getJson())
                )
                .andExpect(status().isBadRequest())
                .andReturn();

        final var resultJson = result.getResponse().getContentAsString();
        final var contentResponse = objectMapper.readValue(resultJson, ValidationErrorResponse.class);

        assertAll(() -> contentResponse.errors().forEach((key, value) -> {
            assertEquals(key, fieldExpected);
            assertEquals(value, messageExpected);
        }));
    }

    @Test
    @WithMockUser
    @DisplayName("Register New Topic Given Author ID Is Null Should Return 400 Bad Request")
    void registerNewTopic_givenAuthorIDIsNull_shouldReturn400BadRequest() throws Exception {
        final var fieldExpected = "authorId";
        final var messageExpected = "Deve ser informado o autor do tópico.";
        final var requestData = new RegisterTopicRequest("Titulo", "Mensagem", null, 1L);

        final var result = mockMvc.perform(
                        post("/topicos")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json.write(requestData).getJson())
                )
                .andExpect(status().isBadRequest())
                .andReturn();

        final var resultJson = result.getResponse().getContentAsString();
        final var contentResponse = objectMapper.readValue(resultJson, ValidationErrorResponse.class);

        assertAll(() -> contentResponse.errors().forEach((key, value) -> {
            assertEquals(key, fieldExpected);
            assertEquals(value, messageExpected);
        }));
    }

    @Test
    @WithMockUser
    @DisplayName("Register New Topic Given Course ID Is Null Should Return 400 Bad Request")
    void registerNewTopic_givenCourseIDIsNull_shouldReturn400BadRequest() throws Exception {
        final var fieldExpected = "courseId";
        final var messageExpected = "Deve ser informado o autor do tópico.";
        final var requestData = new RegisterTopicRequest("Titulo", "Mensagem", 1L, null);

        final var result = mockMvc.perform(
                        post("/topicos")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json.write(requestData).getJson())
                )
                .andExpect(status().isBadRequest())
                .andReturn();

        final var resultJson = result.getResponse().getContentAsString();
        final var contentResponse = objectMapper.readValue(resultJson, ValidationErrorResponse.class);

        assertAll(() -> contentResponse.errors().forEach((key, value) -> {
            assertEquals(key, fieldExpected);
            assertEquals(value, messageExpected);
        }));
    }

    private RegisterTopicRequest buildRegisterTopicRequest() {
        return new RegisterTopicRequest(
                "Titulo do tópico",
                "Mensagem do tópico",
                1L,
                1L
        );
    }

    private RegisterTopicResponse buildRegisterTopicResponse(final LocalDateTime creationDate) {
        return new RegisterTopicResponse(
                1L,
                "Titulo do tópico",
                "Mensagem do tópico",
                creationDate,
                "aberto",
                new SimpleAuthorResponse(1L, "Nome do(a) Autor(a)"),
                new SimpleCourseResponse(1L, "Nome do Curso")
        );
    }

}