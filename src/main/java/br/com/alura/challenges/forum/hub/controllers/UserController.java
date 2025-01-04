package br.com.alura.challenges.forum.hub.controllers;

import br.com.alura.challenges.forum.hub.models.requests.RegisterUserRequest;
import br.com.alura.challenges.forum.hub.models.responses.RegisterUserResponse;
import br.com.alura.challenges.forum.hub.repositories.UserRepository;
import br.com.alura.challenges.forum.hub.services.RegisterUserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/usuarios")
public class UserController {

    private final RegisterUserService registerService;
    private final UserRepository userRepository;

    public UserController(final RegisterUserService registerService,
                          final UserRepository userRepository) {
        this.registerService = registerService;
        this.userRepository = userRepository;
    }

    @PostMapping
    public ResponseEntity<RegisterUserResponse> register(@RequestBody @Valid RegisterUserRequest request) {
        final var result = registerService.execute(request);
        var uri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/usuarios/{username}")
                .buildAndExpand(result.email())
                .toUri();
        return ResponseEntity.created(uri).body(result);
    }

}
