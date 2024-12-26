package br.com.alura.challenges.forum.hub.models.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.HashSet;
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
    private String status;

    @ManyToOne
    @JoinColumn(name = "autor_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "curso_id")
    private Course course;

    @OneToMany
    @JoinTable(name = "respostas_do_topico",
               joinColumns = @JoinColumn(name = "topico_id"),
               inverseJoinColumns = @JoinColumn(name = "resposta_id"))
    private Set<Response> responses = new HashSet<>();

    protected Topic() {
    }

    public Topic(Long id) {
        this.id = id;
    }

    public Topic(String title, String message, LocalDateTime creationDate, TopicStatus status, User author,
                 Course course, Set<Response> responses) {
        this.title = title;
        this.message = message;
        this.creationDate = creationDate;
        this.status = status;
        this.user = user;
        this.course = course;
        this.responses = responses;
    }

    public UUID getId() {
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

    public String getStatus() {
        return status;
    }

    public User getUser() {
        return user;
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

}
