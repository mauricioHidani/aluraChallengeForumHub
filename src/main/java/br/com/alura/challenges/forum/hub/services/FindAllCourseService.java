package br.com.alura.challenges.forum.hub.services;

import br.com.alura.challenges.forum.hub.exceptions.NotFoundException;
import br.com.alura.challenges.forum.hub.models.responses.FindCourseResponse;
import br.com.alura.challenges.forum.hub.repositories.CourseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class FindAllCourseService {

    private final CourseRepository repository;

    public FindAllCourseService(final CourseRepository repository) {
        this.repository = repository;
    }

    public Page<FindCourseResponse> execute(Pageable pageable) {
        final var found = repository.findAll(pageable);
        if (found.isEmpty()) {
            throw new NotFoundException(
                    "Não foram encontrados cursos registrados."
            );
        }

        return new PageImpl<>(found.stream()
                .map(FindCourseResponse::parse)
                .collect(Collectors.toList())
        );
    }

}
