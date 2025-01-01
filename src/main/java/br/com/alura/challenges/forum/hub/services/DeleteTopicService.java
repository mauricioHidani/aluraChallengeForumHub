package br.com.alura.challenges.forum.hub.services;

import br.com.alura.challenges.forum.hub.exceptions.NotFoundException;
import br.com.alura.challenges.forum.hub.repositories.TopicRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DeleteTopicService {

    private final TopicRepository repository;

    public DeleteTopicService(final TopicRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public void execute(Long id) {
        if (!repository.existsById(id)) {
            throw new NotFoundException(
                    "Não foi possivel encontrar o tópico especificado."
            );
        }

        repository.deleteById(id);
    }

}
