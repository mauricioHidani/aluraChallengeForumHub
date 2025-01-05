package br.com.alura.challenges.forum.hub.models.responses;

import br.com.alura.challenges.forum.hub.models.entities.Role;

public record UpdateRoleResponse(
        Long id,
        String name
) {

    public static UpdateRoleResponse parse(Role entity) {
        return new UpdateRoleResponse(entity.getId(), entity.getName());
    }

}
