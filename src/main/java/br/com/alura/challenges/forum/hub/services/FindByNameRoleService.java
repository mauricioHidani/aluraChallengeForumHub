package br.com.alura.challenges.forum.hub.services;

import br.com.alura.challenges.forum.hub.exceptions.NotFoundException;
import br.com.alura.challenges.forum.hub.models.responses.FindRoleResponse;
import br.com.alura.challenges.forum.hub.repositories.RoleRepository;
import org.springframework.stereotype.Service;

@Service
public class FindByNameRoleService {

    private final RoleRepository repository;

    public FindByNameRoleService(final RoleRepository repository) {
        this.repository = repository;
    }

    public FindRoleResponse execute(String name) {
        final var roleName = String.format("ROLE_%s", name);
        final var found = repository.findByName(roleName)
                .orElseThrow(() -> new NotFoundException(String.format(
                        "Não foi encontrado o perfil com o nome %s.", name
                )));

        return FindRoleResponse.parse(found);
    }

}
