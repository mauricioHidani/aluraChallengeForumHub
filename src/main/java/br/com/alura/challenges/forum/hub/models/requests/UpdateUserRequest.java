package br.com.alura.challenges.forum.hub.models.requests;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Set;

public record UpdateUserRequest(
        @Size(min = 3, max = 128, message = "O campo nome deve ter entre 3 à 128 caracteres")
        String name,

        @Size(min = 4, max = 255, message = "O campo email deve ter entre 4 à 255 caracteres")
        String email,

        @Size(min = 6, message = "O campo senha não pode ser menor do que 6 caracteres")
        String password,

        @NotNull(message = "Os perfis não podem estar vázios")
        Set<String> roles
) {
}
