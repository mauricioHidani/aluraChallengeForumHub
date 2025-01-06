package br.com.alura.challenges.forum.hub.models.requests;

import br.com.alura.challenges.forum.hub.models.entities.Course;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdateCourseRequest(
        @NotNull(message = "O nome do curso é um campo obrigatório.")
        @Size(min = 3, max = 128, message = "O nome do curso deve ter entre 3 à 128 caracteres.")
        String name,

        @NotNull(message = "A categoria do curso deve ser informada.")
        @Size(min = 3, max = 128, message = "A categoria deve ter entre 3 à 128 caracteres.")
        String category
) {

    public Course parseToEntity() {
        return new Course(
                name,
                category
        );
    }

}
