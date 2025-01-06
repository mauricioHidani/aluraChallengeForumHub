package br.com.alura.challenges.forum.hub.models.requests;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdateTopicRequest(
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

        @NotNull
        String status,

        @NotNull(message = "Deve ser informado o autor do tópico.")
        Long authorId,

        @NotNull(message = "Deve ser informado o autor do tópico.")
        Long courseId
) {
}
