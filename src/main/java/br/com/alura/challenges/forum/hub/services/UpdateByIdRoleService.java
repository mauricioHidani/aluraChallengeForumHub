package br.com.alura.challenges.forum.hub.services;

import br.com.alura.challenges.forum.hub.exceptions.NotFoundException;
import br.com.alura.challenges.forum.hub.models.entities.Role;
import br.com.alura.challenges.forum.hub.models.requests.UpdateRoleRequest;
import br.com.alura.challenges.forum.hub.models.responses.UpdateRoleResponse;
import br.com.alura.challenges.forum.hub.repositories.RoleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UpdateByIdRoleService {

    private final RoleRepository repository;

    public UpdateByIdRoleService(final RoleRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public UpdateRoleResponse execute(Long id, UpdateRoleRequest request) {
        final var found = repository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format(
                        "Não foi possivel encontrar o perfil com o ID %d especificado.", id
                )));

        final var updated = repository.save(updateData(found, request));
        return UpdateRoleResponse.parse(updated);
    }

    private Role updateData(Role found, UpdateRoleRequest request) {
        final var name = !found.getName().equals(request.name()) ? request.name() : found.getName();

        return new Role(found.getId(), name);
    }

}
