package br.com.alura.challenges.forum.hub.services;

import br.com.alura.challenges.forum.hub.exceptions.NotFoundException;
import br.com.alura.challenges.forum.hub.exceptions.UnauthorizedRequisitionException;
import br.com.alura.challenges.forum.hub.models.entities.Course;
import br.com.alura.challenges.forum.hub.models.entities.Topic;
import br.com.alura.challenges.forum.hub.models.entities.User;
import br.com.alura.challenges.forum.hub.models.enums.TopicStatus;
import br.com.alura.challenges.forum.hub.models.requests.UpdateTopicRequest;
import br.com.alura.challenges.forum.hub.models.responses.UpdateTopicResponse;
import br.com.alura.challenges.forum.hub.repositories.TopicRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UpdateByIdTopicService {

    private final TopicRepository repository;

    public UpdateByIdTopicService(final TopicRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public UpdateTopicResponse execute(Long id, UpdateTopicRequest request, Authentication authentication) {
        final var authUsername = authentication.getName();
        var founded = repository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                    "Não foi encontrado o tópico com o id especificado."
                ));

        final var author = founded.getAuthor();
        if (author == null || !author.getUsername().equals(authUsername)) {
            throw new UnauthorizedRequisitionException(
                "Não permitido que uma pessoa que não seja o autor modifique o tópico."
            );
        }

        var changed = updateData(founded, request);
        if (changed.equals(founded)) {
            return UpdateTopicResponse.parse(founded);
        }

        var updated = repository.save(changed);
        return UpdateTopicResponse.parse(updated);
    }

    private Topic updateData(Topic founded, UpdateTopicRequest updated) {
        var title = !founded.getTitle().equals(updated.title()) ? updated.title() : founded.getTitle();
        var message = !founded.getMessage().equals(updated.message()) ? updated.message() : founded.getMessage();
        var creationDate = founded.getCreationDate();
        var status = !founded.getStatus().getDescription().equals(updated.status()) ? TopicStatus.getByDescription(updated.status()) : founded.getStatus();
        var author = !founded.getAuthor().getId().equals(updated.authorId()) ? new User(updated.authorId()) : founded.getAuthor();
        var course = !founded.getCourse().getId().equals(updated.courseId()) ? new Course(updated.courseId()) : founded.getCourse();
        var responses = founded.getResponses();

        return new Topic(
                founded.getId(),
                title,
                message,
                creationDate,
                status,
                author,
                course,
                responses
        );
    }

}
