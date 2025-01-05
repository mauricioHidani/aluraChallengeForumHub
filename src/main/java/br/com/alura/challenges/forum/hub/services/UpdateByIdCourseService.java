package br.com.alura.challenges.forum.hub.services;

import br.com.alura.challenges.forum.hub.exceptions.NotFoundException;
import br.com.alura.challenges.forum.hub.models.entities.Course;
import br.com.alura.challenges.forum.hub.models.requests.UpdateCourseRequest;
import br.com.alura.challenges.forum.hub.models.responses.UpdateCourseResponse;
import br.com.alura.challenges.forum.hub.repositories.CourseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UpdateByIdCourseService {

    private final CourseRepository repository;

    public UpdateByIdCourseService(final CourseRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public UpdateCourseResponse execute(Long id, UpdateCourseRequest request) {
        final var found = repository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format(
                        "Não foi encontrado o curso com o ID %d especificado.", id
                )));

        final var saved = repository.save(updateData(found, request));
        return UpdateCourseResponse.parse(saved);
    }

    private Course updateData(Course found, UpdateCourseRequest request) {
        final var name = !found.getName().equals(request.name()) ? request.name() : found.getName();
        final var category = !found.getCategory().equals(request.category()) ? request.category() : found.getCategory();

        return new Course(found.getId(), name, category);
    }

}
