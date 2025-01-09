package br.com.alura.challenges.forum.hub.services;

import br.com.alura.challenges.forum.hub.exceptions.NotFoundException;
import br.com.alura.challenges.forum.hub.exceptions.UnauthorizedRequisitionException;
import br.com.alura.challenges.forum.hub.repositories.ResponseRepository;
import br.com.alura.challenges.forum.hub.repositories.TopicRepository;
import br.com.alura.challenges.forum.hub.repositories.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
public class DeleteByIdUserService {

    private final UserRepository repository;
    private final TopicRepository topicRepository;
    private final ResponseRepository responseRepository;

    public DeleteByIdUserService(final UserRepository repository,
                                 final TopicRepository topicRepository,
                                 final ResponseRepository responseRepository) {
        this.repository = repository;
        this.topicRepository = topicRepository;
        this.responseRepository = responseRepository;
    }

    @Transactional
    public void execute(Long id, Authentication authentication) {
        final var authUsername = authentication.getName();
        final var found = repository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                    "Não foi encontrado o usuário informado"
                ));

        if (!Objects.equals(found.getEmail(), authUsername)) {
            throw new UnauthorizedRequisitionException(
                "Está operação não é válida."
            );
        }
        repository.deleteUserRolesAssociated(id);
        topicRepository.deactivateAuthorById(id);
        responseRepository.deactivateAuthorById(id);
        repository.deleteById(id);
    }

}
