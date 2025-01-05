package br.com.alura.challenges.forum.hub.services;

import br.com.alura.challenges.forum.hub.exceptions.NotFoundException;
import br.com.alura.challenges.forum.hub.models.responses.FindResponseResponse;
import br.com.alura.challenges.forum.hub.repositories.ResponseRepository;
import org.springframework.stereotype.Service;

@Service
public class FindByIdResponseService {

    private final ResponseRepository repository;

    public FindByIdResponseService(final ResponseRepository repository) {
        this.repository = repository;
    }

    public FindResponseResponse execute(Long id) {
        final var result = repository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        "Não foi possivel encontrar a resposta com o Id especificado."
                ));

        return FindResponseResponse.parse(result);
    }

}
