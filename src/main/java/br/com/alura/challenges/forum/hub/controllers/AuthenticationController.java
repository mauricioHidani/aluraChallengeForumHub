package br.com.alura.challenges.forum.hub.controllers;

import br.com.alura.challenges.forum.hub.models.entities.User;
import br.com.alura.challenges.forum.hub.models.requests.AuthenticationRequest;
import br.com.alura.challenges.forum.hub.models.responses.TokenJWTResponse;
import br.com.alura.challenges.forum.hub.services.TokenService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class AuthenticationController {

    private final AuthenticationManager manager;
    private final TokenService tokenService;

    public AuthenticationController(final AuthenticationManager manager,
                                    final TokenService tokenService) {
        this.manager = manager;
        this.tokenService = tokenService;
    }

    @PostMapping
    public ResponseEntity<TokenJWTResponse> applyLongin(@RequestBody @Valid AuthenticationRequest request) {
        final var authenticationToken = new UsernamePasswordAuthenticationToken(request.username(), request.password());

        final var authentication = manager.authenticate(authenticationToken);
        final var tokenJWT = tokenService.buildToken((User) authentication.getPrincipal());

        return ResponseEntity.ok(new TokenJWTResponse(tokenJWT));
    }

}
