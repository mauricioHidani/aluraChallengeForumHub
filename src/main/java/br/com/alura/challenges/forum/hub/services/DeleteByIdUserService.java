package br.com.alura.challenges.forum.hub.services;

import br.com.alura.challenges.forum.hub.exceptions.NotFoundException;
import br.com.alura.challenges.forum.hub.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DeleteByIdUserService {

    private final UserRepository repository;

    public DeleteByIdUserService(final UserRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public void execute(Long id) {
        if (!repository.existsById(id)) {
            throw new NotFoundException(
                    "Não foi encontrado o usuário informado"
            );
        }

        repository.deleteById(id);
    }

}
