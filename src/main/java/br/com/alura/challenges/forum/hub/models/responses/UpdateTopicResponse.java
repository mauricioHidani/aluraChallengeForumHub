package br.com.alura.challenges.forum.hub.models.responses;

import br.com.alura.challenges.forum.hub.models.entities.Topic;

import java.time.LocalDateTime;

public record UpdateTopicResponse(
        Long id,
        String title,
        String message,
        LocalDateTime creationDate,
        String status,
        Long authorId,
        Long courseId
) {

    public static UpdateTopicResponse parse(Topic entity) {
        return new UpdateTopicResponse(
                entity.getId(),
                entity.getTitle(),
                entity.getMessage(),
                entity.getCreationDate(),
                entity.getStatus().getDescription(),
                entity.getAuthor().getId(),
                entity.getCourse().getId()
        );
    }

}
