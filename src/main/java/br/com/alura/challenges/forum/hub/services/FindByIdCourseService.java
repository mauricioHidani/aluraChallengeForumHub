package br.com.alura.challenges.forum.hub.services;

import br.com.alura.challenges.forum.hub.exceptions.NotFoundException;
import br.com.alura.challenges.forum.hub.models.responses.FindCourseResponse;
import br.com.alura.challenges.forum.hub.repositories.CourseRepository;
import org.springframework.stereotype.Service;

@Service
public class FindByIdCourseService {

    private final CourseRepository repository;

    public FindByIdCourseService(final CourseRepository repository) {
        this.repository = repository;
    }

    public FindCourseResponse execute(Long id) {
        final var found = repository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format(
                        "Curso não encontrado com o ID %d especificado.", id
                )));

        return FindCourseResponse.parse(found);
    }

}
