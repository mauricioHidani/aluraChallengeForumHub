package br.com.alura.challenges.forum.hub.models.requests;

import br.com.alura.challenges.forum.hub.models.entities.Course;
import br.com.alura.challenges.forum.hub.models.entities.Response;
import br.com.alura.challenges.forum.hub.models.entities.Topic;
import br.com.alura.challenges.forum.hub.models.entities.User;
import br.com.alura.challenges.forum.hub.models.enums.TopicStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.HashSet;

public record RegisterTopicRequest(
        @NotNull(message = "O titulo do tópico é um campo obrigatório.")
        @Size(min = 3,
              max = 128,
              message = "O titulo deve conter entre 3 à 128 caracteres.")
        String title,

        @NotNull(message = "A mensagem do tópico é um campo obrigatório.")
        @Size(min = 3,
              max = 16000,
              message = "A mensagem deve conter 3 à 16000 caracteres.")
        String message,

        @NotNull(message = "Deve ser informado o autor do tópico.")
        Long authorId,

        @NotNull(message = "Deve ser informado o autor do tópico.")
        Long courseId
) {

    public Topic parseToEntity(final TopicStatus status) {
        var creationDate = LocalDateTime.now(Clock.systemDefaultZone());
        var author = new User(this.authorId);
        var course = new Course(this.courseId);
        var responses = new HashSet<Response>();

        return new Topic(
                this.title,
                this.message,
                creationDate,
                status,
                author,
                course,
                responses
        );
    }

}
