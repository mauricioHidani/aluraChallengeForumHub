package br.com.alura.challenges.forum.hub.services;

import br.com.alura.challenges.forum.hub.exceptions.NotFoundException;
import br.com.alura.challenges.forum.hub.exceptions.UnauthorizedRequisitionException;
import br.com.alura.challenges.forum.hub.repositories.ResponseRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DeleteByIdResponseService {

    private final ResponseRepository repository;

    public DeleteByIdResponseService(final ResponseRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public void execute(Long id, Authentication authentication) {
        final var authUsername = authentication.getName();
        final var found = repository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                    "Não foi possivel encontrar a resposta pelo Id."
                ));

        if (!found.getAuthor().getUsername().equals(authUsername)) {
            throw new UnauthorizedRequisitionException(String.format(
                "Está operação não é válida para o usuário %s.", authUsername
            ));
        }

        repository.deleteById(id);
    }

}
