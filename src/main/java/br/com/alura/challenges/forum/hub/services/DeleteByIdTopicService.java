package br.com.alura.challenges.forum.hub.services;

import br.com.alura.challenges.forum.hub.exceptions.NotFoundException;
import br.com.alura.challenges.forum.hub.exceptions.UnauthorizedRequisitionException;
import br.com.alura.challenges.forum.hub.repositories.TopicRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DeleteByIdTopicService {

    private final TopicRepository repository;

    public DeleteByIdTopicService(final TopicRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public void execute(Long id, Authentication authentication) {
        final var authUsername = authentication.getName();
        final var authorities = authentication.getAuthorities();
        final var found = repository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        "Não foi possivel encontrar o tópico especificado."
                ));

        if (found.getAuthor() == null) {
            final var adminAuthority = new SimpleGrantedAuthority("ROLE_ADMIN");
            if (authorities.contains(adminAuthority)) {
                repository.deleteById(id);
                return;
            }

        } else if (found.getAuthor().getEmail().equals(authUsername)) {
            repository.deleteById(id);
            return;
        }

        throw new UnauthorizedRequisitionException(String.format(
                "Não é possível remover o Tópico indicado pelo ID %d.", id
        ));
    }

}
