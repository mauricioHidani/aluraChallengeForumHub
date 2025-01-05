package br.com.alura.challenges.forum.hub.controllers;

import br.com.alura.challenges.forum.hub.models.requests.RegisterCourseRequest;
import br.com.alura.challenges.forum.hub.models.requests.UpdateCourseRequest;
import br.com.alura.challenges.forum.hub.models.responses.FindCourseResponse;
import br.com.alura.challenges.forum.hub.models.responses.RegisterCourseResponse;
import br.com.alura.challenges.forum.hub.models.responses.UpdateCourseResponse;
import br.com.alura.challenges.forum.hub.services.*;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/cursos")
public class CourseController {

    private final RegisterCourseService registerService;
    private final UpdateByIdCourseService updateByIdService;
    private final FindByIdCourseService findByIdService;
    private final FindAllCourseService findAllService;
    private final DeleteByIdCourseService deleteByIdService;

    public CourseController(final RegisterCourseService registerService,
                            final UpdateByIdCourseService updateByIdService,
                            final FindByIdCourseService findByIdService,
                            final FindAllCourseService findAllService,
                            final DeleteByIdCourseService deleteByIdService) {
        this.registerService = registerService;
        this.updateByIdService = updateByIdService;
        this.findByIdService = findByIdService;
        this.findAllService = findAllService;
        this.deleteByIdService = deleteByIdService;
    }

    @PostMapping
    public ResponseEntity<RegisterCourseResponse> register(@RequestBody @Valid RegisterCourseRequest request) {
        final var result = registerService.execute(request);
        final var uri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/cursos/{id}")
                .buildAndExpand(result.id())
                .toUri();

        return ResponseEntity.created(uri).body(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UpdateCourseResponse> updateById(@PathVariable Long id,
                                                           @RequestBody @Valid UpdateCourseRequest request) {
        final var result = updateByIdService.execute(id, request);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FindCourseResponse> findById(@PathVariable Long id) {
        final var result = findByIdService.execute(id);
        return ResponseEntity.ok(result);
    }

    @GetMapping
    public ResponseEntity<Page<FindCourseResponse>> findAll(@PageableDefault(page = 0, size = 16) Pageable pageable) {
        final var result = findAllService.execute(pageable);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        deleteByIdService.execute(id);
        return ResponseEntity.noContent().build();
    }

}
