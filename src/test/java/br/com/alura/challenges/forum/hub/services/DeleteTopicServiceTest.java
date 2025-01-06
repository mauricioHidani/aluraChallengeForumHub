package br.com.alura.challenges.forum.hub.services;

import br.com.alura.challenges.forum.hub.exceptions.NotFoundException;
import br.com.alura.challenges.forum.hub.repositories.TopicRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class DeleteTopicServiceTest {

    @Mock
    private TopicRepository repository;

    @InjectMocks
    private DeleteTopicService service;

    @Test
    @DisplayName("Delete By Id Given Found Topic By Id Should Delete And Return Nothing")
    void deleteById_givenFoundTopicById_shouldDeleteAndReturnNothing() {
        final var id = 1L;

        when(repository.existsById(id)).thenReturn(true);

        service.execute(id);

        verify(repository).existsById(any(Long.class));
        verify(repository).deleteById(any(Long.class));
    }

    @Test
    @DisplayName("Delete By Id Given Not Found Topic By Id Should Return Not Found Exception")
    void deleteById_givenNotFoundTopicById_shouldNotFoundException() {
        final var id = 1000L;

        when(repository.existsById(id)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> service.execute(id));

        verify(repository, times(1)).existsById(any(Long.class));
        verify(repository, never()).deleteById(any(Long.class));
    }

}
