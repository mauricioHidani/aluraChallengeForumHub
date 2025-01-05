package br.com.alura.challenges.forum.hub.models.responses;

import br.com.alura.challenges.forum.hub.models.entities.Response;

public record FindResponseResponse(
        String solution,
        SimpleAuthorResponse author
) {

    public static FindResponseResponse parse(Response entity) {
        return new FindResponseResponse(
                entity.getSolution(),
                SimpleAuthorResponse.parse(entity.getAuthor())
        );
    }

}
