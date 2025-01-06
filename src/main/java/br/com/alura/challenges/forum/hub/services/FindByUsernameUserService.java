package br.com.alura.challenges.forum.hub.services;

import br.com.alura.challenges.forum.hub.exceptions.NotFoundException;
import br.com.alura.challenges.forum.hub.models.responses.FindUserResponse;
import br.com.alura.challenges.forum.hub.repositories.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class FindByUsernameUserService {

    private final UserRepository repository;

    public FindByUsernameUserService(final UserRepository repository) {
        this.repository = repository;
    }

    public FindUserResponse execute(String username) {
        if (username.isBlank() || username.length() < 3) {
            throw new IllegalArgumentException(
                    "Não é possível prosseguir com o nome de usuário informado."
            );
        }
        final var result = repository.findByEmail(username)
                .orElseThrow(() -> new NotFoundException(
                        "Não foi encontrado o usuário com ("+username+") informado."
                ));

        return FindUserResponse.parse(result);
    }

}
