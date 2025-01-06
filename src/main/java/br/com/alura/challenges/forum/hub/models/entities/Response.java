package br.com.alura.challenges.forum.hub.models.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "respostas")
public class Response {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "data_criacao")
    private LocalDateTime creationDate;

    @Column(name = "solucao",
            columnDefinition = "TEXT")
    private String solution;

    @ManyToOne
    @JoinColumn(name = "topico_id")
    private Topic topic;

    @ManyToOne
    @JoinColumn(name = "autor_id")
    private User author;

    protected Response() {
    }

    public Response(LocalDateTime creationDate,
                    String solution,
                    Topic topic,
                    User author) {
        this.creationDate = creationDate;
        this.solution = solution;
        this.topic = topic;
        this.author = author;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public String getSolution() {
        return solution;
    }

    public Topic getTopic() {
        return topic;
    }

    public User getAuthor() {
        return author;
    }

}
