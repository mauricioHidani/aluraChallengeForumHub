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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class FindAllTopicServiceTest {

    @Mock
    private TopicRepository topicRepository;

    @InjectMocks
    private FindAllTopicService service;

    private LocalDateTime crrCreationDate;

    @BeforeEach
    void setUp() throws Exception {
        this.crrCreationDate = LocalDateTime.of(2024, 12, 25, 17, 30, 22);
    }

    @Test
    @DisplayName("Find All Given Paged Defined Should Return Paged Find Topic Response")
    void findAll_givenPagedDefined_shouldReturnPagedFindTopicResponse() {
        final Map<String, String> params = Map.of();
        final var pageable = PageRequest.of(0, 10, Sort.by("name"));

        when(topicRepository.findAll(any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(buildSingleTopic())));

        final var result = service.execute(params, pageable);

        verify(topicRepository, times(1)).findAll(any(Pageable.class));

        assertNotNull(result);
        assertInstanceOf(Page.class, result);
        assertInstanceOf(FindTopicResponse.class, result.stream().findFirst().get());

        assertFalse(result.isEmpty());
    }

    @Test
    @DisplayName("Find All When Not Founded Should Return Not Found Exception")
    void findAll_whenNotFounded_shouldReturnNotFoundException() {
        final var exceptionMessage = "Não foram encontrados tópicos com as especificações indicadas.";
        final Map<String, String> params = Map.of();
        final var pageable = PageRequest.of(0, 10, Sort.by("name"));

        when(topicRepository.findAll(any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of()));

        final var result = assertThrows(
                NotFoundException.class,
                () -> service.execute(params, pageable)
        );

        assertEquals(exceptionMessage, result.getMessage());
    }

    @Test
    @DisplayName("Find All Given First 10 Topics By Creation Date Should Return Paged Find Topic Response")
    void findAll_givenFirst10TopicsByCreationDate_shouldReturnPagedFindTopicResponse() {
        final var mostRecentDate = this.crrCreationDate;
        final Map<String, String> params = Map.of();
        final var pageable = PageRequest.of(0, 10, Sort.by("creationDate"));

        when(topicRepository.findTop10ByOrderByCreationDateAsc())
                .thenReturn(List.of(buildSingleTopic()));

        final var result = service.execute(params, pageable);

        verify(topicRepository, times(1)).findTop10ByOrderByCreationDateAsc();

        assertNotNull(result);
        assertInstanceOf(Page.class, result);
        assertInstanceOf(FindTopicResponse.class, result.stream().findFirst().get());

        assertEquals(mostRecentDate, result.stream().findFirst().get().creationDate());
    }

    @Test
    @DisplayName("Find All Given First 10 Topics By Creation Date Not Founded Should Return Not Found Exception")
    void findAll_givenFirst10TopicsByCreationDateNotFounded_shouldReturnNotFoundException() {
        final var exceptionMessage = "Não foram encontrados tópicos com as especificações indicadas.";
        final Map<String, String> params = Map.of();
        final var pageable = PageRequest.of(0, 10, Sort.by("creationDate"));

        when(topicRepository.findTop10ByOrderByCreationDateAsc())
                .thenReturn(List.of());

        final var result = assertThrows(
                NotFoundException.class,
                () -> service.execute(params, pageable)
        );

        assertEquals(exceptionMessage, result.getMessage());
    }

    @Test
    @DisplayName("Find All Given Params Has Course Name Should Return Paged Find Topic Response")
    void findAll_givenParamsHasCourseName_shouldReturnPagedFindTopicResponse() {
        final var courseParam = "course";
        final var target = "Curso Spring JPA";
        final Map<String, String> params = Map.of(courseParam, target);
        final var pageable = Pageable.unpaged();

        when(topicRepository.findByCourse_Name(target, pageable))
                .thenReturn(new PageImpl<>(List.of(buildSingleTopic())));

        final var result = service.execute(params, pageable);

        verify(topicRepository, times(1))
                .findByCourse_Name(any(String.class), any(Pageable.class));

        assertNotNull(result);
        assertInstanceOf(Page.class, result);
        assertInstanceOf(FindTopicResponse.class, result.stream().findFirst().get());

        assertAll(() -> result.forEach(r -> assertEquals(target, r.course().name())));
    }

    @Test
    @DisplayName("Find All Given Params Has Course Name Not Founded Should Return Not Found Exception")
    void findAll_givenParamsHasCourseNameNotFounded_shouldReturnNotFoundException() {
        final var exceptionMessage = "Não foram encontrados tópicos com as especificações indicadas.";
        final var courseParam = "course";
        final var target = "Spring JPA";
        final Map<String, String> params = Map.of(courseParam, target);
        final var pageable = Pageable.unpaged();

        when(topicRepository.findByCourse_Name(target, pageable))
                .thenReturn(new PageImpl<>(List.of()));

        final var result = assertThrows(
                NotFoundException.class,
                () -> service.execute(params, pageable)
        );

        assertEquals(exceptionMessage, result.getMessage());
    }

    @Test
    @DisplayName("Find All Given Params Has Creation Year Should Return Paged Find Topic Response")
    void findAll_givenParamsHasCreationYear_shouldReturnPagedFindTopicResponse() {
        final var creationYear = "creation-year";
        final var target = "2024";
        final Map<String, String> params = Map.of(creationYear, target);
        final var pageable = Pageable.unpaged();

        when(topicRepository.findAllByCreationDateYear(Integer.parseInt(target)))
                .thenReturn(List.of(buildSingleTopic()));

        final var result = service.execute(params, pageable);

        verify(topicRepository, times(1))
                .findAllByCreationDateYear(any(Integer.class));

        assertNotNull(result);
        assertInstanceOf(Page.class, result);
        assertInstanceOf(FindTopicResponse.class, result.stream().findFirst().get());

        assertAll(() -> result.forEach(r -> assertEquals(Integer.parseInt(target), r.creationDate().getYear())));
    }

    @Test
    @DisplayName("Find All Given Params Has Creation Year Not Founded Should Return Not Found Exception")
    void findAll_givenParamsHasCreationYearNotFounded_shouldReturnNotFoundException() {
        final var exceptionMessage = "Não foram encontrados tópicos com as especificações indicadas.";
        final var creationYear = "creation-year";
        final var target = "3000";
        final Map<String, String> params = Map.of(creationYear, target);
        final var pageable = Pageable.unpaged();

        when(topicRepository.findAllByCreationDateYear(Integer.parseInt(target)))
                .thenReturn(List.of());

        final var result = assertThrows(
                NotFoundException.class,
                () -> service.execute(params, pageable)
        );

        assertEquals(exceptionMessage, result.getMessage());
    }

    private Topic buildSingleTopic() {
        return new Topic(
            1L,
            "Spring JPA",
            "Como construir query methods",
            crrCreationDate,
            TopicStatus.OPENED,
            new User(1L),
            new Course(1L, "Curso Spring JPA"),
            Set.of()
        );
    }

}