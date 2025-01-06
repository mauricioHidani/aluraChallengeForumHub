package br.com.alura.challenges.forum.hub.models.entities;

import br.com.alura.challenges.forum.hub.models.enums.TopicStatus;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "topicos")
public class Topic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "titulo",
            columnDefinition = "VARCHAR(128)",
            nullable = false)
    private String title;

    @Column(name = "mensagem",
            columnDefinition = "VARCHAR(16000)")
    private String message;

    @Column(name = "data_criacao")
    private LocalDateTime creationDate;

    @Column(name = "status",
            columnDefinition = "VARCHAR(128)")
    @Enumerated(EnumType.STRING)
    private TopicStatus status;

    @ManyToOne
    @JoinColumn(name = "autor_id")
    private User author;

    @ManyToOne
    @JoinColumn(name = "curso_id")
    private Course course;

    @OneToMany(mappedBy = "topic",
               cascade = CascadeType.ALL,
               orphanRemoval = true)
    private Set<Response> responses = new HashSet<>();

    protected Topic() {
    }

    public Topic(Long id) {
        this.id = id;
    }

    public Topic(String title,
                 String message,
                 LocalDateTime creationDate,
                 TopicStatus status,
                 User author,
                 Course course,
                 Set<Response> responses) {
        this.title = title;
        this.message = message;
        this.creationDate = creationDate;
        this.status = status;
        this.author = author;
        this.course = course;
        this.responses = responses;
    }

    public Topic(Long id,
                 String title,
                 String message,
                 LocalDateTime creationDate,
                 TopicStatus status,
                 User author,
                 Course course,
                 Set<Response> responses) {
        this.id = id;
        this.title = title;
        this.message = message;
        this.creationDate = creationDate;
        this.status = status;
        this.author = author;
        this.course = course;
        this.responses = responses;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public TopicStatus getStatus() {
        return status;
    }

    public void setStatus(TopicStatus status) {
        this.status = status;
    }

    public User getAuthor() {
        return author;
    }

    public Course getCourse() {
        return course;
    }

    public Set<Response> getResponses() {
        return responses;
    }

    public void addResponse(Response response) {
        this.responses.add(response);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Topic topic = (Topic) o;
        return Objects.equals(id, topic.id) &&
                Objects.equals(title, topic.title) &&
                Objects.equals(message, topic.message) &&
                Objects.equals(author.getId(), topic.author.getId()) &&
                Objects.equals(course.getId(), topic.course.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, message, author, course);
    }

}
