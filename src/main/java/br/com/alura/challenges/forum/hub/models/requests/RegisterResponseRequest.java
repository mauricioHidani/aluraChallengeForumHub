package br.com.alura.challenges.forum.hub.models.requests;

import br.com.alura.challenges.forum.hub.models.entities.Response;
import br.com.alura.challenges.forum.hub.models.entities.Topic;
import br.com.alura.challenges.forum.hub.models.entities.User;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record RegisterResponseRequest(
        @NotNull(message = "A resposta com a solução deve ser informada.")
        @Size(min = 3, message = "Deve conter no minimo 3 caracteres.")
        String solution,

        @NotNull(message = "O Identificador do Tópico deve ser informado.")
        Long topicId,

        @NotNull(message = "O identificado do Autor deve ser informado.")
        Long authorId
) {

    public Response parseToEntity(LocalDateTime creationDate, Topic topic, User author) {
        return new Response(
            creationDate,
            solution,
            topic,
            author
        );
    }

}
