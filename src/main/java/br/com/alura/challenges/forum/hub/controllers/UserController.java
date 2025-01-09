package br.com.alura.challenges.forum.hub.controllers;

import br.com.alura.challenges.forum.hub.models.requests.RegisterUserRequest;
import br.com.alura.challenges.forum.hub.models.requests.UpdateUserRequest;
import br.com.alura.challenges.forum.hub.models.responses.FindUserResponse;
import br.com.alura.challenges.forum.hub.models.responses.RegisterUserResponse;
import br.com.alura.challenges.forum.hub.models.responses.UpdateUserResponse;
import br.com.alura.challenges.forum.hub.services.DeleteByIdUserService;
import br.com.alura.challenges.forum.hub.services.FindByUsernameUserService;
import br.com.alura.challenges.forum.hub.services.RegisterUserService;
import br.com.alura.challenges.forum.hub.services.UpdateByIdUserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/usuarios")
public class UserController {

    private final RegisterUserService registerService;
    private final UpdateByIdUserService updateByIdService;
    private final FindByUsernameUserService findByUsernameService;
    private final DeleteByIdUserService deleteByIdService;

    public UserController(final RegisterUserService registerService,
                          final UpdateByIdUserService updateByIdService,
                          final FindByUsernameUserService findByUsernameService,
                          final DeleteByIdUserService deleteByIdService) {
        this.registerService = registerService;
        this.updateByIdService = updateByIdService;
        this.findByUsernameService = findByUsernameService;
        this.deleteByIdService = deleteByIdService;
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

    @PutMapping("/{id}")
    public ResponseEntity<UpdateUserResponse> updateById(@PathVariable Long id,
                                                         @RequestBody @Valid UpdateUserRequest request) {
        final var result = updateByIdService.execute(id, request);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{username}")
    public ResponseEntity<FindUserResponse> findByUsername(@PathVariable String username) {
        final var result = findByUsernameService.execute(username);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id, Authentication authentication) {
        deleteByIdService.execute(id, authentication);
        return ResponseEntity.noContent().build();
    }

}
