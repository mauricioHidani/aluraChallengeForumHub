package br.com.alura.challenges.forum.hub.models.requests;

import br.com.alura.challenges.forum.hub.models.entities.Role;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RegisterRoleRequest(
        @NotNull(message = "O nome do perfil é obrigatório.")
        @Size(min = 3, max = 128, message = "O nome do perfil deve ter entre 3 à 128 caracteres.")
        String name
) {

    public Role parseToEntity() {
        final var roleName = String.format("ROLE_%s", name);
        return new Role(roleName);
    }

}
