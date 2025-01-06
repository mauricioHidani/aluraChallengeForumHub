package br.com.alura.challenges.forum.hub.models.responses;

import br.com.alura.challenges.forum.hub.models.entities.Course;

public record RegisterCourseResponse(
        Long id,
        String name,
        String category
) {

    public static RegisterCourseResponse parse(Course entity) {
        return new RegisterCourseResponse(
                entity.getId(),
                entity.getName(),
                entity.getCategory()
        );
    }

}
