package br.com.alura.challenges.forum.hub.services;

import br.com.alura.challenges.forum.hub.exceptions.NotFoundException;
import br.com.alura.challenges.forum.hub.models.responses.FindRoleResponse;
import br.com.alura.challenges.forum.hub.repositories.RoleRepository;
import org.springframework.stereotype.Service;

@Service
public class FindByIdRoleService {

    private final RoleRepository repository;

    public FindByIdRoleService(final RoleRepository repository) {
        this.repository = repository;
    }

    public FindRoleResponse execute(Long id) {
        final var found = repository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format(
                        "Não foi possivel encontrar o perfil com o ID %d especificado.", id
                )));

        return FindRoleResponse.parse(found);
    }

}
