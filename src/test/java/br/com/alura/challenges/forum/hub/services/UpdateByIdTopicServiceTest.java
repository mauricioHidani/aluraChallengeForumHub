package br.com.alura.challenges.forum.hub.services;

import br.com.alura.challenges.forum.hub.exceptions.NotFoundException;
import br.com.alura.challenges.forum.hub.exceptions.UnauthorizedRequisitionException;
import br.com.alura.challenges.forum.hub.models.entities.Course;
import br.com.alura.challenges.forum.hub.models.entities.Role;
import br.com.alura.challenges.forum.hub.models.entities.Topic;
import br.com.alura.challenges.forum.hub.models.entities.User;
import br.com.alura.challenges.forum.hub.models.enums.TopicStatus;
import br.com.alura.challenges.forum.hub.models.requests.UpdateTopicRequest;
import br.com.alura.challenges.forum.hub.models.responses.UpdateTopicResponse;
import br.com.alura.challenges.forum.hub.repositories.TopicRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class UpdateByIdTopicServiceTest {

    @Mock
    private TopicRepository repository;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private UpdateByIdTopicService service;

    private PasswordEncoder passwordEncoder;
    private String password;

    @BeforeEach
    void setUp() throws Exception {
        passwordEncoder = new BCryptPasswordEncoder();
        password = passwordEncoder.encode("123456");
    }

    @Test
    @DisplayName("Update By Id Given Topic Found By Id And Updated Should Return Update Topic Response")
    void updateById_givenTopicFoundByIdAndUpdated_shouldReturnUpdateTopicResponse() {
        final var id = 1L;
        final var userId = 1L;
        final var logged = buildUser(userId, "fulana.user@gmail.com", "ROLE_USER");
        final var request = buildUpdateTopicRequest();
        final var found = buildTopic(id, logged);

        when(authentication.getName()).thenReturn(logged.getUsername());
        when(repository.findById(eq(id))).thenReturn(Optional.of(found));
        when(repository.save(any(Topic.class))).thenAnswer(invocation -> invocation.getArgument(0));

        final var result = service.execute(id, request, authentication);

        verify(repository, times(1)).findById(any(Long.class));
        verify(repository, times(1)).save(any(Topic.class));
        verify(authentication, times(1)).getName();

        assertThat(result).isNotNull();
        assertThat(result).isInstanceOf(UpdateTopicResponse.class);
        assertThat(result.authorId()).isEqualTo(userId);
    }

    @Test
    @DisplayName("Update By Id Given Topic Not Found By Id Should Return Not Found Exception")
    void updateById_givenTopicNotFoundById_shouldReturnNotFoundException() {
        final var exceptionMessage = "Não foi encontrado o tópico com o id especificado.";
        final var id = 1000L;
        final var request = buildUpdateTopicRequest();

        when(repository.findById(id)).thenReturn(Optional.empty());

        final var result = assertThrows(NotFoundException.class, () -> service.execute(id, request, authentication));

        verify(repository, times(1)).findById(any(Long.class));
        verify(repository, never()).save(any(Topic.class));

        assertThat(result.getMessage()).isEqualTo(exceptionMessage);
    }

    @Test
    @DisplayName("Update By Id Given Topic Found By Id And No Data Updated Should Return Update Topic Response")
    void updateById_givenTopicFoundByIdAndNoDataUpdated_shouldReturnUpdateTopicResponse() {
        final var id = 1L;
        final var userId = 1L;
        final var logged = buildUser(userId, "fulana.user@gmail.com", "ROLE_USER");
        final var request = buildNoUpdateTopicRequest();
        final var existingTopic = buildTopic(id, logged);

        when(repository.findById(id)).thenReturn(Optional.of(existingTopic));
        when(authentication.getName()).thenReturn(logged.getUsername());

        final var result = service.execute(id, request, authentication);

        verify(repository, times(1)).findById(id);
        verify(repository, never()).save(any(Topic.class));
        verify(authentication, times(1)).getName();

        assertThat(result).isNotNull();
        assertThat(result.title()).isEqualTo(existingTopic.getTitle());
        assertThat(result.message()).isEqualTo(existingTopic.getMessage());
    }

    @Test
    @DisplayName("Update By Id Given Topic Found By Id And Is Not An Owner Of Topic Should Return Unauthorized Requisition Exception")
    void updateById_givenTopicFoundByIdAndIsNotAnOwnerOfTopic_shouldReturnUnauthorizedRequisitionException() {
        final var exceptionMessage = "Não permitido que uma pessoa que não seja o autor modifique o tópico.";
        final var id = 1L;
        final var logged = buildUser(2L, "beltrana.user@gmail.com", "ROLE_USER");
        final var owner = buildUser(1L, "fulana.user@gmail.com", "ROLE_USER");
        final var found = buildTopic(id, owner);
        final var request = buildUpdateTopicRequest();

        when(authentication.getName()).thenReturn(logged.getUsername());
        when(repository.findById(id)).thenReturn(Optional.of(found));

        final var result = assertThrows(
                UnauthorizedRequisitionException.class,
                () -> service.execute(id, request, authentication)
        );

        verify(authentication, times(1)).getName();
        verify(repository, times(1)).findById(any(Long.class));
        verify(repository, never()).save(any(Topic.class));

        assertThat(result.getMessage()).isEqualTo(exceptionMessage);
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

    Topic buildTopic(final Long id, final User author) {
        return new Topic(
                id,
                "Tópico antigo",
                "Mensagem do tópico antigo",
                LocalDateTime.of(2024, 12, 30, 12, 30, 22),
                TopicStatus.OPENED,
                author,
                new Course(1L, "Curso Spring JPA"),
                Set.of()
        );
    }

    User buildUser(final Long id, final String username, final String roleName) {
        final var user = new User(
                id,
                "Fulana Test",
                username,
                password
        );
        user.addRole(new Role(roleName));

        return user;
    }

}