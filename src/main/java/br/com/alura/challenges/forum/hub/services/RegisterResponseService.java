package br.com.alura.challenges.forum.hub.services;

import br.com.alura.challenges.forum.hub.exceptions.NotFoundException;
import br.com.alura.challenges.forum.hub.models.requests.RegisterResponseRequest;
import br.com.alura.challenges.forum.hub.models.responses.RegisterResponseResponse;
import br.com.alura.challenges.forum.hub.repositories.ResponseRepository;
import br.com.alura.challenges.forum.hub.repositories.TopicRepository;
import br.com.alura.challenges.forum.hub.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDateTime;

@Service
public class RegisterResponseService {

    private final ResponseRepository repository;
    private final TopicRepository topicRepository;
    private final UserRepository userRepository;

    public RegisterResponseService(final ResponseRepository repository,
                                   final TopicRepository topicRepository,
                                   final UserRepository userRepository) {
        this.repository = repository;
        this.topicRepository = topicRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public RegisterResponseResponse execute(RegisterResponseRequest request) {
        final var creationDate = LocalDateTime.now(Clock.systemDefaultZone());

        final var topicFound = topicRepository.findById(request.topicId())
                .orElseThrow(() -> new NotFoundException(
                        "Não é possivel responder o tópico especificado, ele não foi encontrado."
                ));
        final var authorFound = userRepository.findById(request.authorId())
                .orElseThrow(() -> new NotFoundException(
                        "Não é possivel identificar o autor da resposta."
                ));

        final var parsedToEntity = request.parseToEntity(creationDate, topicFound, authorFound);
        final var saved = repository.save(parsedToEntity);

        return RegisterResponseResponse.parse(saved);
    }

}
