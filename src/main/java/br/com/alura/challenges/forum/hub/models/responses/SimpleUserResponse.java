package br.com.alura.challenges.forum.hub.models.responses;

import br.com.alura.challenges.forum.hub.models.entities.User;

public record SimpleUserResponse(
        Long id,
        String name
) {

    public static SimpleUserResponse parse(final User user) {
        return new SimpleUserResponse(user.getId(), user.getName());
    }

}
