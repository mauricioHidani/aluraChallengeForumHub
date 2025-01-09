package br.com.alura.challenges.forum.hub.services;

import br.com.alura.challenges.forum.hub.exceptions.NotFoundException;
import br.com.alura.challenges.forum.hub.exceptions.UnauthorizedRequisitionException;
import br.com.alura.challenges.forum.hub.models.entities.Course;
import br.com.alura.challenges.forum.hub.models.entities.Role;
import br.com.alura.challenges.forum.hub.models.entities.Topic;
import br.com.alura.challenges.forum.hub.models.entities.User;
import br.com.alura.challenges.forum.hub.models.enums.TopicStatus;
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
import java.util.Collection;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class DeleteByIdTopicServiceTest {

    @Mock
    private TopicRepository repository;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private DeleteByIdTopicService service;

    private PasswordEncoder passwordEncoder;

    private String password;

    @BeforeEach
    void setUp() throws Exception {
        passwordEncoder = new BCryptPasswordEncoder();
        password = passwordEncoder.encode("123456");
    }

    @Test
    @DisplayName("Delete By Id Given Logged User Is Topic Owner Found By Id Should Delete And Return Nothing")
    void deleteById_givenLoggedUserIsTopicOwnerFoundById_shouldDeleteAndReturnNothing() {
        final var id = 1L;
        final var logged = buildUser(1L, "fulana.user@gmail.com", "ROLE_USER");
        final var found = buildTopic(id, logged);

        when(repository.findById(id)).thenReturn(Optional.of(found));
        when(authentication.getName()).thenReturn(logged.getEmail());
        when(authentication.getAuthorities()).thenReturn((Collection) logged.getAuthorities());

        service.execute(id, authentication);

        verify(repository, times(1)).findById(any(Long.class));
        verify(repository, times(1)).deleteById(any(Long.class));
        verify(authentication).getName();
        verify(authentication).getAuthorities();

        assertThat(authentication.getName()).isEqualTo(logged.getEmail());
        assertThat(authentication.getAuthorities()).isEqualTo(logged.getAuthorities());
    }

    @Test
    @DisplayName("Delete By Id Given Logged Is An Admin And Is Not The Owner Of The Topic Should Delete And Return Nothing")
    void deleteById_givenLoggedIsAnAdminAndIsNotTheOwnerOfTheTopic_shouldDeleteAndReturnNothing() {
        final var id = 1L;
        final var userLogged = buildUser(1L, "fulana.admin@gmail.com", "ROLE_ADMIN");
        final var found = buildTopic(id, null);

        when(repository.findById(id)).thenReturn(Optional.of(found));
        when(authentication.getName()).thenReturn(userLogged.getEmail());
        when(authentication.getAuthorities()).thenReturn((Collection) userLogged.getAuthorities());

        service.execute(id, authentication);

        verify(repository, times(1)).findById(any(Long.class));
        verify(repository, times(1)).deleteById(any(Long.class));
        verify(authentication).getName();
        verify(authentication).getAuthorities();

        assertThat(authentication.getName()).isEqualTo(userLogged.getEmail());
        assertThat(authentication.getAuthorities()).isEqualTo(userLogged.getAuthorities());
    }

    @Test
    @DisplayName("Delete By Id Given Logged In An User And Is Not The Owner Of The Topic Should Return Unauthorized Requisition Exception")
    void deleteById_givenLoggedInAnUserAndIsNotTheOwnerOfTheTopic_shouldReturnUnauthorizedRequisitionException() {
        final var id = 1L;
        final var exceptionMessage = String.format("Não é possível remover o Tópico indicado pelo ID %d.", id);
        final var logged = buildUser(1L, "fulana.user@gmail.com", "ROLE_USER");
        final var owner = buildUser(3L, "beltrana.user@gmail.com", "ROLE_USER");
        final var found = buildTopic(id, owner);

        when(repository.findById(id)).thenReturn(Optional.of(found));
        when(authentication.getName()).thenReturn(logged.getEmail());
        when(authentication.getAuthorities()).thenReturn((Collection) logged.getAuthorities());

        var result = assertThrows(UnauthorizedRequisitionException.class, () -> service.execute(id, authentication));

        verify(repository, times(1)).findById(any(Long.class));
        verify(repository, never()).deleteById(any(Long.class));
        verify(authentication, times(1)).getName();
        verify(authentication, times(1)).getAuthorities();

        assertThat(result.getMessage()).isEqualTo(exceptionMessage);
        assertThat(result).isInstanceOf(UnauthorizedRequisitionException.class);
    }

    @Test
    @DisplayName("Delete By Id Given Not Found Topic By Id Should Return Not Found Exception")
    void deleteById_givenNotFoundTopicById_shouldNotFoundException() {
        final var id = 1000L;
        final var logged = buildUser(1L, "fulana.test@gmail.com", "ROLE_USER");

        when(repository.existsById(id)).thenReturn(false);
        when(authentication.getName()).thenReturn(logged.getEmail());
        when(authentication.getAuthorities()).thenReturn((Collection) logged.getAuthorities());

        assertThrows(NotFoundException.class, () -> service.execute(id, authentication));

        verify(repository, times(1)).findById(any(Long.class));
        verify(repository, never()).deleteById(any(Long.class));
        verify(authentication).getName();
        verify(authentication).getAuthorities();
    }

    Topic buildTopic(final Long id, final User author) {
        return new Topic(
                id,
                "Titulo",
                "Mensagem",
                LocalDateTime.of(2025, 1, 3, 10, 33, 22),
                TopicStatus.OPENED,
                author,
                new Course(1L),
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
