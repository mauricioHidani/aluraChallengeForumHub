package br.com.alura.challenges.forum.hub.services;

import br.com.alura.challenges.forum.hub.exceptions.NotFoundException;
import br.com.alura.challenges.forum.hub.models.entities.Course;
import br.com.alura.challenges.forum.hub.models.entities.Topic;
import br.com.alura.challenges.forum.hub.models.entities.User;
import br.com.alura.challenges.forum.hub.models.enums.TopicStatus;
import br.com.alura.challenges.forum.hub.models.requests.UpdateTopicRequest;
import br.com.alura.challenges.forum.hub.models.responses.UpdateTopicResponse;
import br.com.alura.challenges.forum.hub.repositories.TopicRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class UpdateTopicServiceTest {

    @Mock
    private TopicRepository repository;

    @InjectMocks
    private UpdateTopicService service;

    @Test
    @DisplayName("Update By Id Given Topic Found By Id And Updated Should Return Update Topic Response")
    void updateById_givenTopicFoundByIdAndUpdated_shouldReturnUpdateTopicResponse() {
        final var id = 1L;
        final var request = buildUpdateTopicRequest();
        final var existingTopic = buildTopic(id);

        when(repository.findById(id)).thenReturn(Optional.of(existingTopic));
        when(repository.save(any(Topic.class))).thenAnswer(invocation -> invocation.getArgument(0));

        final var result = service.execute(id, request);

        assertThat(result).isNotNull();
        assertThat(result).isInstanceOf(UpdateTopicResponse.class);

        verify(repository, times(1)).findById(any(Long.class));
        verify(repository, times(1)).save(any(Topic.class));
    }

    @Test
    @DisplayName("Update By Id Given Topic Not Found By Id Should Return Not Found Exception")
    void updateById_givenTopicNotFoundById_shouldReturnNotFoundException() {
        final var id = 1000L;
        final var request = buildUpdateTopicRequest();

        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> service.execute(id, request));
        verify(repository, times(1)).findById(any(Long.class));
        verify(repository, never()).save(any(Topic.class));
    }

    @Test
    @DisplayName("Update By Id Given Topic Found By Id And No Data Updated Should Return Update Topic Response")
    void updateById_givenTopicFoundByIdAndNoDataUpdated_shouldReturnUpdateTopicResponse() {
        final var id = 1L;
        final var request = buildNoUpdateTopicRequest();
        final var existingTopic = buildTopic(id);

        when(repository.findById(id)).thenReturn(Optional.of(existingTopic));
        when(repository.save(any(Topic.class))).thenAnswer(invocation -> invocation.getArgument(0));

        final var result = service.execute(id, request);

        assertThat(result).isNotNull();
        assertThat(result.title()).isEqualTo(existingTopic.getTitle());
        assertThat(result.message()).isEqualTo(existingTopic.getMessage());

        verify(repository, times(1)).findById(id);
        verify(repository, never()).save(any(Topic.class));
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

    UpdateTopicRequest buildNoUpdateTopicRequest() {
        return new UpdateTopicRequest(
                "Tópico antigo",
                "Mensagem do tópico antigo",
                TopicStatus.OPENED.getDescription(),
                1L,
                1L
        );
    }

    Topic buildTopic(final Long id) {
        return new Topic(
                id,
                "Tópico antigo",
                "Mensagem do tópico antigo",
                LocalDateTime.of(2024, 12, 30, 12, 30, 22),
                TopicStatus.OPENED,
                new User(1L),
                new Course(1L, "Curso Spring JPA"),
                Set.of()
        );
    }

}