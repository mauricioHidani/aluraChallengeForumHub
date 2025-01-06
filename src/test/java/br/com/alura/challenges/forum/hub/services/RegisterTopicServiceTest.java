package br.com.alura.challenges.forum.hub.services;

import br.com.alura.challenges.forum.hub.models.entities.Course;
import br.com.alura.challenges.forum.hub.models.entities.Topic;
import br.com.alura.challenges.forum.hub.models.entities.User;
import br.com.alura.challenges.forum.hub.models.enums.TopicStatus;
import br.com.alura.challenges.forum.hub.models.requests.RegisterTopicRequest;
import br.com.alura.challenges.forum.hub.models.responses.RegisterTopicResponse;
import br.com.alura.challenges.forum.hub.models.transfers.SimpleFindTopicTransfer;
import br.com.alura.challenges.forum.hub.repositories.TopicRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class RegisterTopicServiceTest {

    @InjectMocks
    private RegisterTopicService registerTopicService;

    @Mock
    private TopicRepository topicRepository;

    @Mock
    private RegisterTopicRequest registerTopicRequest;

    @Test
    @DisplayName("Register Topic Given Successful Should Return A Register Topic Response")
    void registerTopic_givenSuccessful_shouldReturnARegisterTopicResponse() {
        final Long savedId = 1L;
        final var creationDate = LocalDateTime.now(Clock.systemDefaultZone()).minusSeconds(1);

        final var topicRequest = buildTopicRequest();
        final var topicEntityParsed = buildTopicEntity(creationDate, topicRequest);
        final var saveEntity = new Topic(savedId);
        final var topicEntitySaved = buildTopicTransfer(savedId, topicEntityParsed);

        when(topicRepository.existsByTitleAndMessage(topicRequest.title(), topicRequest.message())).thenReturn(false);
        when(registerTopicRequest.parseToEntity(TopicStatus.OPENED)).thenReturn(topicEntityParsed);
        when(topicRepository.save(any(Topic.class))).thenReturn(saveEntity);
        when(topicRepository.findTopicDetailsById(savedId)).thenReturn(topicEntitySaved);

        final var result = registerTopicService.execute(topicRequest);

        verify(topicRepository, times(1)).existsByTitleAndMessage(any(String.class), any(String.class));
        verify(topicRepository, times(1)).save(any(Topic.class));
        verify(topicRepository, times(1)).findTopicDetailsById(any(Long.class));

        assertNotNull(result);
        assertInstanceOf(RegisterTopicResponse.class, result);
    }

    @Test
    @DisplayName("Register Topic Given Title And Message Exists Should Return Illegal Argument Exception")
    void registerTopic_givenTitleAndMessageExists_shouldReturnIllegalArgumentException() {
        final var expectedMessage = "O tópico já existe, não é possível criar um tópico existente.";
        final var titleExists = "Title already exists";
        final var messageExists = "Message already exists";
        final var topicTitleExists = new RegisterTopicRequest(titleExists, messageExists, 1L, 1L);

        when(topicRepository.existsByTitleAndMessage(titleExists, messageExists)).thenReturn(true);

        var resultThrows = assertThrows(
                IllegalArgumentException.class,
                () -> registerTopicService.execute(topicTitleExists)
        );
        verify(topicRepository, times(1))
                .existsByTitleAndMessage(any(String.class), any(String.class));
        assertEquals(expectedMessage, resultThrows.getMessage());
    }

    private RegisterTopicRequest buildTopicRequest() {
        return new RegisterTopicRequest(
                "Example of title",
                "Example of message",
                1L,
                1L
        );
    }

    private Topic buildTopicEntity(final LocalDateTime creationDate, final RegisterTopicRequest request) {
        return new Topic(
                request.title(),
                request.message(),
                creationDate,
                TopicStatus.OPENED,
                new User(1L, "Fulano de Souza"),
                new Course(1L, "Curso exemplo"),
                Set.of()
        );
    }

    private SimpleFindTopicTransfer buildTopicTransfer(final Long id, final Topic entity) {
        return new SimpleFindTopicTransfer(){
            @Override public Long getId() { return id; }
            @Override public String getTitle() { return entity.getTitle(); }
            @Override public String getMessage() { return entity.getMessage(); }
            @Override public LocalDateTime getCreationDate() { return entity.getCreationDate(); }
            @Override public TopicStatus getStatus() { return entity.getStatus(); }
            @Override public Long getAuthorId() { return entity.getAuthor().getId(); }
            @Override public String getAuthorName() { return entity.getAuthor().getName(); }
            @Override public Long getCourseId() { return entity.getCourse().getId(); }
            @Override public String getCourseName() { return entity.getCourse().getName(); }
        };
    }

}