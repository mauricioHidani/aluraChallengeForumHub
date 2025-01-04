package br.com.alura.challenges.forum.hub.models.responses;

import br.com.alura.challenges.forum.hub.models.entities.User;

public record FindUserResponse(
        String name,
        String username
) {

    public static FindUserResponse parse(final User entity) {
        return new FindUserResponse(
                entity.getName(),
                entity.getEmail()
        );
    }

}
