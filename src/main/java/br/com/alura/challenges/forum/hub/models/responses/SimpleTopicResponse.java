package br.com.alura.challenges.forum.hub.models.responses;

import br.com.alura.challenges.forum.hub.models.entities.Topic;

import java.time.LocalDateTime;

public record SimpleTopicResponse(
        Long id,
        String title,
        LocalDateTime creationDate,
        String status
) {

    public static SimpleTopicResponse parse(Topic entity) {
        return new SimpleTopicResponse(
                entity.getId(),
                entity.getTitle(),
                entity.getCreationDate(),
                entity.getStatus().getDescription()
        );
    }

}
