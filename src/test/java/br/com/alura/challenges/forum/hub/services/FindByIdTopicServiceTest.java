package br.com.alura.challenges.forum.hub.services;

import br.com.alura.challenges.forum.hub.exceptions.NotFoundException;
import br.com.alura.challenges.forum.hub.models.entities.Course;
import br.com.alura.challenges.forum.hub.models.entities.Topic;
import br.com.alura.challenges.forum.hub.models.entities.User;
import br.com.alura.challenges.forum.hub.models.enums.TopicStatus;
import br.com.alura.challenges.forum.hub.models.responses.FindTopicResponse;
import br.com.alura.challenges.forum.hub.repositories.TopicRepository;
import org.junit.jupiter.api.BeforeEach;
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
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class FindByIdTopicServiceTest {

    @Mock
    private TopicRepository repository;

    @InjectMocks
    private FindByIdTopicService service;

    private Long existId;

    @BeforeEach
    void setUp() throws Exception {
        existId = 1L;
    }

    @Test
    @DisplayName("Find By Id Given Exists By Id Should Return Find Topic Response")
    void findById_givenExistsById_shouldReturnFindTopicResponse() {
        final var expectedFound = buildSingleTopic();

        when(repository.findById(existId)).thenReturn(Optional.of(expectedFound));

        final var result = service.execute(existId);

        verify(repository, times(1)).findById(any());

        assertThat(result).isNotNull();
        assertThat(result).isInstanceOf(FindTopicResponse.class);
    }

    @Test
    @DisplayName("Find By Id Given No Exists By Id Should Return Not Found Exception")
    void findById_givenNoExistsById_shouldReturnNotFoundException() {
        final var exceptionMessage = "Não foi possivel encontrar o tópico pelo id.";
        final var expectedId = 10000L;

        when(repository.findById(existId)).thenReturn(Optional.empty());

        final var result = assertThrows(NotFoundException.class, () -> service.execute(expectedId));

        verify(repository, times(1)).findById(any());

        assertThat(result.getMessage()).isEqualTo(exceptionMessage);
    }

    private Topic buildSingleTopic() {
        return new Topic(
                existId,
                "Spring JPA",
                "Como construir query methods",
                LocalDateTime.of(2024, 12, 25, 17, 30, 22),
                TopicStatus.OPENED,
                new User(1L),
                new Course(1L, "Curso Spring JPA"),
                Set.of()
        );
    }

}
