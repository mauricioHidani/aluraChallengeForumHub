package br.com.alura.challenges.forum.hub.services;

import br.com.alura.challenges.forum.hub.exceptions.NotFoundException;
import br.com.alura.challenges.forum.hub.models.responses.FindRoleResponse;
import br.com.alura.challenges.forum.hub.repositories.RoleRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class FindAllRoleService {

    private final RoleRepository repository;

    public FindAllRoleService(final RoleRepository repository) {
        this.repository = repository;
    }

    public Page<FindRoleResponse> execute(Pageable pageable) {
        final var found = repository.findAll(pageable);
        if (found.isEmpty()) {
            throw new NotFoundException(
                    "Não foi possivel encontrar perfis cadastrados."
            );
        }

        return new PageImpl<>(found.stream()
                .map(FindRoleResponse::parse)
                .collect(Collectors.toList())
        );
    }

}
