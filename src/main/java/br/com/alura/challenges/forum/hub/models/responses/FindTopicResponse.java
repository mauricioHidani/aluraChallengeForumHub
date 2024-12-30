package br.com.alura.challenges.forum.hub.models.responses;

import br.com.alura.challenges.forum.hub.models.entities.Topic;

import java.time.LocalDateTime;

public record FindTopicResponse(
        String title,
        String message,
        LocalDateTime creationDate,
        String status,
        SimpleUserResponse author,
        SimpleCourseResponse course
) {

    public static FindTopicResponse parse(final Topic topic) {
        return new FindTopicResponse(
                topic.getTitle(),
                topic.getMessage(),
                topic.getCreationDate(),
                topic.getStatus().getDescription(),
                SimpleUserResponse.parse(topic.getAuthor()),
                SimpleCourseResponse.parse(topic.getCourse())
        );
    }

}
