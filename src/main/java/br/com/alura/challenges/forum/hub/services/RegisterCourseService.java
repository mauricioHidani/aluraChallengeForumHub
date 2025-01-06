package br.com.alura.challenges.forum.hub.services;

import br.com.alura.challenges.forum.hub.exceptions.ConflictEntityException;
import br.com.alura.challenges.forum.hub.models.requests.RegisterCourseRequest;
import br.com.alura.challenges.forum.hub.models.responses.RegisterCourseResponse;
import br.com.alura.challenges.forum.hub.repositories.CourseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RegisterCourseService {

    private final CourseRepository repository;

    public RegisterCourseService(final CourseRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public RegisterCourseResponse execute(RegisterCourseRequest request) {
        if (repository.existsByName(request.name())) {
            throw new ConflictEntityException(
                    "Não é possivel prosseguir com a requisição e registrar o curso indicado."
            );
        }

        final var saved = repository.save(request.parseToEntity());
        return RegisterCourseResponse.parse(saved);
    }

}
