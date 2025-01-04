package br.com.alura.challenges.forum.hub.models.transfers;

import br.com.alura.challenges.forum.hub.models.enums.TopicStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public interface SimpleFindTopicTransfer {
    Long getId();
    String getTitle();
    String getMessage();
    LocalDateTime getCreationDate();
    TopicStatus getStatus();
    Long getAuthorId();
    String getAuthorName();
    Long getCourseId();
    String getCourseName();
}
