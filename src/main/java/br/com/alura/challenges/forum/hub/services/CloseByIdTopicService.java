package br.com.alura.challenges.forum.hub.services;

import br.com.alura.challenges.forum.hub.exceptions.NotFoundException;
import br.com.alura.challenges.forum.hub.exceptions.UnauthorizedRequisitionException;
import br.com.alura.challenges.forum.hub.models.enums.TopicStatus;
import br.com.alura.challenges.forum.hub.repositories.TopicRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CloseByIdTopicService {

    private final TopicRepository repository;

    public CloseByIdTopicService(final TopicRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public String execute(Long id, Authentication authentication) {
        final var authUsername = authentication.getName();
        final var closeStatus = TopicStatus.CLOSED;
        var found = repository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        "Tópico não encontrado pelo identificador especificado."
                ));

        final var author = found.getAuthor();
        if (author == null || !author.getUsername().equals(authUsername)) {
            throw new UnauthorizedRequisitionException(
                "Não permitido que uma pessoa que não seja o autor modifique o tópico."
            );
        }

        found.setStatus(closeStatus);
        return closeStatus.getDescription();
    }

}
