package br.com.alura.challenges.forum.hub.models.responses;

import br.com.alura.challenges.forum.hub.models.entities.User;

public record SimpleAuthorResponse(
        Long id,
        String name
) {

    public static SimpleAuthorResponse parse(final User author) {
        return new SimpleAuthorResponse(
                author.getId(),
                author.getName()
        );
    }

}
