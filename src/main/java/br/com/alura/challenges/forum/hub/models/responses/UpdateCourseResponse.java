package br.com.alura.challenges.forum.hub.models.responses;

import br.com.alura.challenges.forum.hub.models.entities.Course;

public record UpdateCourseResponse(
        Long id,
        String name,
        String category
) {

    public static UpdateCourseResponse parse(Course entity) {
        return new UpdateCourseResponse(
                entity.getId(),
                entity.getName(),
                entity.getCategory()
        );
    }

}
