package br.com.alura.challenges.forum.hub.models.responses;

import br.com.alura.challenges.forum.hub.models.entities.Role;

public record FindRoleResponse(
        Long id,
        String name
) {

    public static FindRoleResponse parse(Role entity) {
        return new FindRoleResponse(entity.getId(), entity.getName());
    }

}
