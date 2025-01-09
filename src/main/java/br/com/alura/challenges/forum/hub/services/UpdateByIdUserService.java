package br.com.alura.challenges.forum.hub.services;

import br.com.alura.challenges.forum.hub.exceptions.NotFoundException;
import br.com.alura.challenges.forum.hub.models.entities.User;
import br.com.alura.challenges.forum.hub.models.requests.UpdateUserRequest;
import br.com.alura.challenges.forum.hub.models.responses.UpdateUserResponse;
import br.com.alura.challenges.forum.hub.repositories.RoleRepository;
import br.com.alura.challenges.forum.hub.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UpdateByIdUserService {

    private final UserRepository repository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UpdateByIdUserService(final UserRepository repository,
                                 final RoleRepository roleRepository,
                                 final PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UpdateUserResponse execute(Long id, UpdateUserRequest request) {
        var found = repository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                    "O usuário não foi encontrado com o identificador especificado ("+id+")."
                ));

        final var updated = repository.save(updateData(found, request));
        return UpdateUserResponse.parse(updated);
    }

    private User updateData(User found, UpdateUserRequest update) {
        var name = !found.getName().equals(update.name()) ? update.name() : found.getName();
        var email = !found.getEmail().equals(update.email()) ? update.email() : found.getEmail();
        var password = !passwordEncoder.matches(update.password(), found.getPassword()) ? passwordEncoder.encode(update.password()) : found.getPassword();

        final var newUser = new User(found.getId(), name, email, password);
        update.roles().stream()
                .map(roleName -> roleRepository.findByName(roleName)
                    .orElseThrow(() -> new NotFoundException(
                            "Não foi possivel encontrar o perfil de usuário especificado."
                        )
                    )
                )
                .forEach(newUser::addRole);

        return newUser;
    }

}
