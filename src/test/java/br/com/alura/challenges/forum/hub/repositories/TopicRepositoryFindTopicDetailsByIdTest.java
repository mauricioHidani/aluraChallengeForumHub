package br.com.alura.challenges.forum.hub.repositories;

import br.com.alura.challenges.forum.hub.models.entities.Course;
import br.com.alura.challenges.forum.hub.models.entities.Role;
import br.com.alura.challenges.forum.hub.models.entities.Topic;
import br.com.alura.challenges.forum.hub.models.entities.User;
import br.com.alura.challenges.forum.hub.models.enums.TopicStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.ActiveProfiles;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TopicRepositoryFindTopicDetailsByIdTest {

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private TestEntityManager em;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    private Role role;
    private User author;
    private Course course;
    private Topic topic;

    @BeforeEach
    void setUp() throws Exception {
        this.role = registerRole();
        this.author = registerAuthor(this.role);
        this.course = registerCourse();
        this.topic = registerTopic(this.author, this.course);
    }

    @Test
    @DisplayName("Find Topic Details By Id Given Successful Should Return Simple Find Topic Data Transfer Object")
    void findTopicDetailsById_givenSuccessful_shouldReturnSimpleFindTopicDataTransferObject() {
        final var result = topicRepository.findTopicDetailsById(this.topic.getId());

        assertNotNull(result);
        assertEquals(topic.getId(), result.getId());
        assertEquals(topic.getTitle(), result.getTitle());
        assertEquals(topic.getMessage(), result.getMessage());
        assertEquals(topic.getAuthor().getId(), result.getAuthorId());
        assertEquals(topic.getCourse().getId(), result.getCourseId());
        assertEquals(topic.getStatus(), result.getStatus());
        assertEquals(topic.getCreationDate().withNano(0), result.getCreationDate());
    }

    @Test
    @DisplayName("Find Topic Details By Id Given Not Found Should Return Null")
    void findTopicDetailsById_givenNotFound_shouldReturnNull() {
        final var result = topicRepository.findTopicDetailsById(1000000L);

        assertNull(result);
    }

    @Commit
    Role registerRole() {
        return em.persistFlushFind(
                new Role("ROLE_USER")
        );
    }

    @Commit
    User registerAuthor(final Role role) {
        return em.persist(
            new User(
                "Vera Joana Luana Aragão",
                "vera.joanalu@email.com",
                passwordEncoder.encode("123456")
            )
        );
    }

    @Commit
    Course registerCourse() {
        return em.persist(
            new Course(
                "Java Spring Web",
                "Backend"
            )
        );
    }

    @Commit
    Topic registerTopic(final User author, final Course course) {
        return em.persist(
            new Topic(
                "Spring JPA",
                "Como testar Repositories usando o JUnit",
                LocalDateTime.now(Clock.systemDefaultZone()).minusMinutes(2),
                TopicStatus.OPENED,
                author,
                course,
                Set.of()
            )
        );
    }

}
