package br.com.alura.challenges.forum.hub.services;

import br.com.alura.challenges.forum.hub.exceptions.NotFoundException;
import br.com.alura.challenges.forum.hub.repositories.RoleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DeleteByIdRoleService {

    private final RoleRepository repository;

    public DeleteByIdRoleService(final RoleRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public void execute(Long id) {
        if (!repository.existsById(id)) {
            throw new NotFoundException(String.format(
                    "Não foi possivel encontrar o perfil com o ID %d especificado.", id
            ));
        }

        repository.deleteById(id);
    }

}
