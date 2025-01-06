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

import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TopicRepositoryFindAllByCreationDateYearTest {

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private TestEntityManager em;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    private LocalDateTime creationDate;
    private Role role;
    private User author;
    private Course course;
    private Topic topic;

    @BeforeEach
    void setUp() throws Exception {
        this.creationDate = LocalDateTime.of(2024, 12, 03, 19, 30, 22);
        this.role = registerRole();
        this.author = registerAuthor(this.role);
        this.course = registerCourse();
        this.topic = registerTopic(this.author, this.course);
    }

    @Test
    @DisplayName("Find All By Creation Date Year Given Successful Should Return List Of Topic Creation Date By Year")
    void findAllByCreationDateYear_givenSuccessful_shouldReturnListOfTopicCreationDateByYear() {
        final var result = topicRepository.findAllByCreationDateYear(creationDate.getYear());

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertAll(() -> result.forEach(r ->
            assertEquals(topic.getCreationDate().getYear(), r.getCreationDate().getYear())
        ));
    }

    @Test
    @DisplayName("Find All By Creation Date By Year Given Not Found Topics By Year Should Return Empty List")
    void findAllByCreationDateByYear_givenNotFoundTopicsByYear_shouldReturnEmptyList() {
        final var result = topicRepository.findAllByCreationDateYear(9001);

        assertTrue(result.isEmpty());
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
                        "Java Spring JPA",
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
                        creationDate,
                        TopicStatus.OPENED,
                        author,
                        course,
                        Set.of()
                )
        );
    }

}
