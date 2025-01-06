package br.com.alura.challenges.forum.hub.services;

import br.com.alura.challenges.forum.hub.models.enums.TopicStatus;
import br.com.alura.challenges.forum.hub.models.requests.RegisterTopicRequest;
import br.com.alura.challenges.forum.hub.models.responses.RegisterTopicResponse;
import br.com.alura.challenges.forum.hub.repositories.TopicRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RegisterTopicService {

    private final TopicRepository topicRepository;

    public RegisterTopicService(final TopicRepository topicRepository) {
        this.topicRepository = topicRepository;
    }

    @Transactional
    public RegisterTopicResponse execute(RegisterTopicRequest request) {
        if (topicRepository.existsByTitleAndMessage(request.title(), request.message())) {
            throw new IllegalArgumentException(
                "O tópico já existe, não é possível criar um tópico existente."
            );
        }

        final var entityParsed = request.parseToEntity(TopicStatus.OPENED);
        final var saveIdResult = topicRepository.save(entityParsed);
        final var result = topicRepository.findTopicDetailsById(saveIdResult.getId());

        return RegisterTopicResponse.parse(result);
    }

}
