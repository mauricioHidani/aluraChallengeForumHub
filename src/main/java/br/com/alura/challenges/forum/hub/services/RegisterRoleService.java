package br.com.alura.challenges.forum.hub.services;

import br.com.alura.challenges.forum.hub.exceptions.ConflictEntityException;
import br.com.alura.challenges.forum.hub.models.requests.RegisterRoleRequest;
import br.com.alura.challenges.forum.hub.models.responses.RegisterRoleResponse;
import br.com.alura.challenges.forum.hub.repositories.RoleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RegisterRoleService {

    private final RoleRepository repository;

    public RegisterRoleService(final RoleRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public RegisterRoleResponse execute(RegisterRoleRequest request) {
        final var roleName = String.format("ROLE_%s", request.name());
        if (repository.existsByName(roleName)) {
            throw new ConflictEntityException(
                    "Não é possivel prosseguir com a operação, já existe um perfil como este."
            );
        }

        final var saved = repository.save(request.parseToEntity());
        return RegisterRoleResponse.parse(saved);
    }

}
