package br.com.alura.challenges.forum.hub.services;

import br.com.alura.challenges.forum.hub.exceptions.NotFoundException;
import br.com.alura.challenges.forum.hub.models.enums.TopicStatus;
import br.com.alura.challenges.forum.hub.repositories.TopicRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CloseByIdTopicService {

    private final TopicRepository repository;

    public CloseByIdTopicService(final TopicRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public String execute(Long id) {
        final var closeStatus = TopicStatus.CLOSED;
        var found = repository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        "Tópico não encontrado pelo identificador especificado."
                ));

        found.setStatus(closeStatus);
        return closeStatus.getDescription();
    }

}
