package br.com.alura.challenges.forum.hub.controllers;

import br.com.alura.challenges.forum.hub.models.requests.RegisterTopicRequest;
import br.com.alura.challenges.forum.hub.services.RegisterTopicService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/topicos")
public class TopicController {

    private final RegisterTopicService registerService;

    public TopicController(final RegisterTopicService registerService) {
        this.registerService = registerService;
    }

    @PostMapping
    public ResponseEntity register(@RequestBody @Valid RegisterTopicRequest request) {
        var result = registerService.execute(request);
        var uri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/topicos/{id}")
                .buildAndExpand(result.id())
                .toUri();
        return ResponseEntity.created(uri).body(result);
    }

}
