package br.com.alura.challenges.forum.hub.services;

import br.com.alura.challenges.forum.hub.exceptions.NotFoundException;
import br.com.alura.challenges.forum.hub.repositories.ResponseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DeleteByIdResponseService {

    private final ResponseRepository repository;

    public DeleteByIdResponseService(final ResponseRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public void execute(Long id) {
        if (!repository.existsById(id)) {
            throw new NotFoundException(
                    "Não foi possivel encontrar a resposta pelo Id."
            );
        }

        repository.deleteById(id);
    }

}
