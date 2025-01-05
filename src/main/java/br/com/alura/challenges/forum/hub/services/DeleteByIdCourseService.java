package br.com.alura.challenges.forum.hub.services;

import br.com.alura.challenges.forum.hub.exceptions.NotFoundException;
import br.com.alura.challenges.forum.hub.repositories.CourseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DeleteByIdCourseService {

    private final CourseRepository repository;

    public DeleteByIdCourseService(final CourseRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public void execute(Long id) {
        if (!repository.existsById(id)) {
            throw new NotFoundException(String.format(
                    "Não foi encontrado o curso com o ID %d especificado.", id
            ));
        }

        repository.deleteById(id);
    }

}
