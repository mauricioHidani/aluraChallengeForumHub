package br.com.alura.challenges.forum.hub.models.requests;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record AuthenticationRequest(
        @NotNull
        @Size(min = 3, max = 255)
        String username,

        @NotNull
        String password
) {
}
