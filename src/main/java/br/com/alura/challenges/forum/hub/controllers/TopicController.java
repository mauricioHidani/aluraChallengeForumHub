package br.com.alura.challenges.forum.hub.controllers;

import br.com.alura.challenges.forum.hub.models.requests.RegisterTopicRequest;
import br.com.alura.challenges.forum.hub.models.responses.FindTopicResponse;
import br.com.alura.challenges.forum.hub.models.responses.RegisterTopicResponse;
import br.com.alura.challenges.forum.hub.services.FindAllTopicService;
import br.com.alura.challenges.forum.hub.services.RegisterTopicService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/topicos")
public class TopicController {

    private final RegisterTopicService registerService;
    private final FindAllTopicService findAllService;

    public TopicController(final RegisterTopicService registerService,
                           final FindAllTopicService findAllService) {
        this.registerService = registerService;
        this.findAllService = findAllService;
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

    @GetMapping
    public ResponseEntity<Page<FindTopicResponse>> findAll(@RequestParam Map<String, String> params,
                                                           @PageableDefault(size = 10, sort = "creationDate") Pageable pageable) {
        for (String paramKey : Set.of("page", "size", "sort")) {
            params.remove(paramKey);
        }

        final var result = findAllService.execute(params, pageable);
        return ResponseEntity.ok(result);
    }

}
