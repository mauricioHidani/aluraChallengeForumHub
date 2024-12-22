package br.com.alura.challenges.forum.hub.models.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "cursos")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome",
            columnDefinition = "VARCHAR(128)")
    private String name;

    @Column(name = "categoria",
            columnDefinition = "VARCHAR(128)")
    private String category;

    protected Course() {
    }

    public Course(String name, String category) {
        this.name = name;
        this.category = category;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

}
