package br.com.alura.challenges.forum.hub.models.responses;

import br.com.alura.challenges.forum.hub.models.entities.Role;

public record RegisterRoleResponse(
        Long id,
        String name
) {

    public static RegisterRoleResponse parse(Role entity) {
        return new RegisterRoleResponse(entity.getId(), entity.getName());
    }

}
