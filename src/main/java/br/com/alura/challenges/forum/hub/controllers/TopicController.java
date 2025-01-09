package br.com.alura.challenges.forum.hub.controllers;

import br.com.alura.challenges.forum.hub.models.requests.RegisterTopicRequest;
import br.com.alura.challenges.forum.hub.models.requests.UpdateTopicRequest;
import br.com.alura.challenges.forum.hub.models.responses.FindTopicResponse;
import br.com.alura.challenges.forum.hub.models.responses.RegisterTopicResponse;
import br.com.alura.challenges.forum.hub.models.responses.UpdateTopicResponse;
import br.com.alura.challenges.forum.hub.services.*;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/topicos")
public class TopicController {

    private final RegisterTopicService registerService;
    private final FindByIdTopicService findByIdService;
    private final FindAllTopicService findAllService;
    private final UpdateByIdTopicService updateByIdService;
    private final DeleteByIdTopicService deleteByIdService;
    private final CloseByIdTopicService closeByIdService;

    public TopicController(final RegisterTopicService registerService,
                           final FindByIdTopicService findByIdService,
                           final FindAllTopicService findAllService,
                           final UpdateByIdTopicService updateByIdService,
                           final DeleteByIdTopicService deleteByIdService,
                           final CloseByIdTopicService closeByIdService) {
        this.registerService = registerService;
        this.findByIdService = findByIdService;
        this.findAllService = findAllService;
        this.updateByIdService = updateByIdService;
        this.deleteByIdService = deleteByIdService;
        this.closeByIdService = closeByIdService;
    }

    @PostMapping
    public ResponseEntity<RegisterTopicResponse> register(@RequestBody @Valid RegisterTopicRequest request) {
        var result = registerService.execute(request);
        var uri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/topicos/{id}")
                .buildAndExpand(result.id())
                .toUri();
        return ResponseEntity.created(uri).body(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FindTopicResponse> findById(@PathVariable Long id) {
        final var result = findByIdService.execute(id);
        return ResponseEntity.ok(result);
    }

    @GetMapping
    public ResponseEntity<Page<FindTopicResponse>> findAll(@RequestParam Map<String, String> params,
                                                           @PageableDefault(size = 10, sort = "creationDate") Pageable pageable) {
        for (String paramKey : Set.of("page", "size", "sort")) {
            params.remove(paramKey);
        }

        final var result = findAllService.execute(params, pageable);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UpdateTopicResponse> updateById(@PathVariable Long id,
                                                          @RequestBody UpdateTopicRequest request,
                                                          Authentication authentication) {
        final var result = updateByIdService.execute(id, request, authentication);
        return ResponseEntity.ok(result);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<String> closeById(@PathVariable Long id,
                                            Authentication authentication) {
        final var result = closeByIdService.execute(id, authentication);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id,
                                           Authentication authentication) {
        deleteByIdService.execute(id, authentication);
        return ResponseEntity.noContent().build();
    }

}
