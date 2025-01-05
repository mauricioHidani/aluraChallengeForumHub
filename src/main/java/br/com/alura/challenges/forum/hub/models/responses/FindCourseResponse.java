package br.com.alura.challenges.forum.hub.models.responses;

import br.com.alura.challenges.forum.hub.models.entities.Course;

public record FindCourseResponse(
        Long id,
        String name,
        String category
) {

    public static FindCourseResponse parse(Course entity) {
        return new FindCourseResponse(
                entity.getId(),
                entity.getName(),
                entity.getCategory()
        );
    }

}
