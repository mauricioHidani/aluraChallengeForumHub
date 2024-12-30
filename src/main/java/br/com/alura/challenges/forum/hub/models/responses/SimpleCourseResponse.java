package br.com.alura.challenges.forum.hub.models.responses;

import br.com.alura.challenges.forum.hub.models.entities.Course;

public record SimpleCourseResponse(
        Long id,
        String name
) {

    public static SimpleCourseResponse parse(final Course course) {
        return new SimpleCourseResponse(course.getId(), course.getName());
    }

}
