package br.com.alura.challenges.forum.hub.services;

import br.com.alura.challenges.forum.hub.exceptions.ConflictEntityException;
import br.com.alura.challenges.forum.hub.exceptions.NotFoundException;
import br.com.alura.challenges.forum.hub.models.requests.RegisterUserRequest;
import br.com.alura.challenges.forum.hub.models.responses.RegisterUserResponse;
import br.com.alura.challenges.forum.hub.repositories.RoleRepository;
import br.com.alura.challenges.forum.hub.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RegisterUserService {

    private final UserRepository repository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public RegisterUserService(final UserRepository repository,
                               final RoleRepository roleRepository,
                               final PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public RegisterUserResponse execute(RegisterUserRequest request) {
        if (repository.existsByEmail(request.email())) {
            throw new ConflictEntityException(
                    "Não é possível cadastrar o usuário"
            );
        }

        final var userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new NotFoundException("Não foi possível encontrar a função do usuário"));

        final var parsedToEntity = request.parseToEntity(passwordEncoder);
        parsedToEntity.addRole(userRole);

        return RegisterUserResponse.parse(
                repository.save(parsedToEntity)
        );
    }

}
