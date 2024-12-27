package br.com.alura.challenges.forum.hub.models.responses;

import java.time.Instant;

public record ErrorResponse(
        Integer statusCode,
        String status,
        Instant timestamp,
        String errorMessage,
        String path
) {

}
