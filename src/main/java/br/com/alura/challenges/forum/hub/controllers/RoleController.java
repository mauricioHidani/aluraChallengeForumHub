package br.com.alura.challenges.forum.hub.controllers;

import br.com.alura.challenges.forum.hub.models.requests.RegisterRoleRequest;
import br.com.alura.challenges.forum.hub.models.requests.UpdateRoleRequest;
import br.com.alura.challenges.forum.hub.models.responses.FindRoleResponse;
import br.com.alura.challenges.forum.hub.models.responses.RegisterRoleResponse;
import br.com.alura.challenges.forum.hub.models.responses.UpdateRoleResponse;
import br.com.alura.challenges.forum.hub.services.*;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/perfis")
public class RoleController {

    private final RegisterRoleService registerService;
    private final UpdateByIdRoleService updateByIdService;
    private final DeleteByIdRoleService deleteByidService;
    private final FindByIdRoleService findByIdService;
    private final FindAllRoleService findAllService;
    private final FindByNameRoleService findByNameService;

    public RoleController(final RegisterRoleService registerService,
                          final UpdateByIdRoleService updateByIdService,
                          final DeleteByIdRoleService deleteByidService,
                          final FindByIdRoleService findByIdService,
                          final FindAllRoleService findAllService,
                          final FindByNameRoleService findByNameService) {
        this.registerService = registerService;
        this.updateByIdService = updateByIdService;
        this.deleteByidService = deleteByidService;
        this.findByIdService = findByIdService;
        this.findAllService = findAllService;
        this.findByNameService = findByNameService;
    }

    @PostMapping
    public ResponseEntity<RegisterRoleResponse> register(@RequestBody @Valid RegisterRoleRequest request) {
        final var result = registerService.execute(request);
        final var uri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/perfis/{id}")
                .buildAndExpand(result.id())
                .toUri();

        return ResponseEntity.created(uri).body(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UpdateRoleResponse> updateById(@PathVariable Long id,
                                                         @RequestBody @Valid UpdateRoleRequest request) {
        final var result = updateByIdService.execute(id, request);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        deleteByidService.execute(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<FindRoleResponse> findById(@PathVariable Long id) {
        final var result = findByIdService.execute(id);
        return ResponseEntity.ok(result);
    }

    @GetMapping
    public ResponseEntity<Page<FindRoleResponse>> findAll(@PageableDefault(page = 0, size = 10) Pageable pageable) {
        final var result = findAllService.execute(pageable);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/nome/{name}")
    public ResponseEntity<FindRoleResponse> findByName(@PathVariable String name) {
        final var result = findByNameService.execute(name);
        return ResponseEntity.ok(result);
    }

}
