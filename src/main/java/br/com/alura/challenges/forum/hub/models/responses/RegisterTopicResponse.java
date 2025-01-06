package br.com.alura.challenges.forum.hub.models.responses;

import br.com.alura.challenges.forum.hub.models.entities.Course;
import br.com.alura.challenges.forum.hub.models.entities.User;
import br.com.alura.challenges.forum.hub.models.transfers.SimpleFindTopicTransfer;

import java.time.LocalDateTime;

public record RegisterTopicResponse(
        Long id,
        String title,
        String message,
        LocalDateTime creationDate,
        String status,
        SimpleAuthorResponse author,
        SimpleCourseResponse course
) {

    public static RegisterTopicResponse parse(final SimpleFindTopicTransfer entity) {
        final var author = new User(entity.getAuthorId(), entity.getAuthorName());
        final var course = new Course(entity.getCourseId(), entity.getCourseName());

        return new RegisterTopicResponse(
                entity.getId(),
                entity.getTitle(),
                entity.getMessage(),
                entity.getCreationDate(),
                entity.getStatus().getDescription(),
                SimpleAuthorResponse.parse(author),
                SimpleCourseResponse.parse(course)
        );
    }

}
