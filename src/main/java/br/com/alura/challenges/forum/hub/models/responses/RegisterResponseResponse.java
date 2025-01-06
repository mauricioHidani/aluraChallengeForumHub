package br.com.alura.challenges.forum.hub.models.responses;

import br.com.alura.challenges.forum.hub.models.entities.Response;

public record RegisterResponseResponse(
        Long id,
        String solution,
        SimpleTopicResponse topic,
        SimpleAuthorResponse author
) {

    public static RegisterResponseResponse parse(Response entity) {
        return new RegisterResponseResponse(
                entity.getId(),
                entity.getSolution(),
                SimpleTopicResponse.parse(entity.getTopic()),
                SimpleAuthorResponse.parse(entity.getAuthor())
        );
    }

}
