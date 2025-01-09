package br.com.alura.challenges.forum.hub.controllers;

import br.com.alura.challenges.forum.hub.models.requests.RegisterResponseRequest;
import br.com.alura.challenges.forum.hub.models.responses.FindResponseResponse;
import br.com.alura.challenges.forum.hub.models.responses.RegisterResponseResponse;
import br.com.alura.challenges.forum.hub.services.DeleteByIdResponseService;
import br.com.alura.challenges.forum.hub.services.FindByIdResponseService;
import br.com.alura.challenges.forum.hub.services.RegisterResponseService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/respostas")
public class ResponseController {

    private final RegisterResponseService registerService;
    private final FindByIdResponseService findByIdService;
    private final DeleteByIdResponseService deleteByIdService;

    public ResponseController(final RegisterResponseService registerService,
                              final FindByIdResponseService findByIdService,
                              final DeleteByIdResponseService deleteByIdService) {
        this.registerService = registerService;
        this.findByIdService = findByIdService;
        this.deleteByIdService = deleteByIdService;
    }

    @PostMapping
    public ResponseEntity<RegisterResponseResponse> register(@RequestBody @Valid RegisterResponseRequest request) {
        var result = registerService.execute(request);
        var uri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/respostas/{id}")
                .buildAndExpand(result.id())
                .toUri();
        return ResponseEntity.created(uri).body(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FindResponseResponse> findById(@PathVariable Long id) {
        var result = findByIdService.execute(id);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id,
                                           Authentication authentication) {
        deleteByIdService.execute(id, authentication);
        return ResponseEntity.noContent().build();
    }

}
