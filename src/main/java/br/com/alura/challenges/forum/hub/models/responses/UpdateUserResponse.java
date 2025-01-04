package br.com.alura.challenges.forum.hub.models.responses;

import br.com.alura.challenges.forum.hub.models.entities.Role;
import br.com.alura.challenges.forum.hub.models.entities.User;

import java.util.Set;
import java.util.stream.Collectors;

public record UpdateUserResponse(
        Long id,
        String name,
        String email,
        Set<String> roles
) {

    public static UpdateUserResponse parse(User entity) {
        return new UpdateUserResponse(
                entity.getId(),
                entity.getName(),
                entity.getEmail(),
                entity.getRoles().stream()
                        .map(Role::getName)
                        .collect(Collectors.toSet())
        );
    }
}
