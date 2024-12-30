package br.com.alura.challenges.forum.hub.services;

import br.com.alura.challenges.forum.hub.exceptions.NotFoundException;
import br.com.alura.challenges.forum.hub.models.responses.FindTopicResponse;
import br.com.alura.challenges.forum.hub.repositories.TopicRepository;
import org.springframework.stereotype.Service;

@Service
public class FindByIdTopicService {

    private final TopicRepository repository;

    public FindByIdTopicService(final TopicRepository repository) {
        this.repository = repository;
    }

    public FindTopicResponse execute(Long id) {
        final var result = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Não foi possivel encontrar o tópico pelo id."));

        return FindTopicResponse.parse(result);
    }

}
