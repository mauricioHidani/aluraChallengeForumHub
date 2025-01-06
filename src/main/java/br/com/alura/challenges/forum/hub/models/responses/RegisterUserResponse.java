package br.com.alura.challenges.forum.hub.models.responses;

import br.com.alura.challenges.forum.hub.models.entities.User;

public record RegisterUserResponse(
        Long id,
        String name,
        String email
) {

    public static RegisterUserResponse parse(final User entity) {
        return new RegisterUserResponse(
                entity.getId(),
                entity.getName(),
                entity.getEmail()
        );
    }

}
