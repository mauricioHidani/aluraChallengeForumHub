package br.com.alura.challenges.forum.hub.services;

import br.com.alura.challenges.forum.hub.exceptions.NotFoundException;
import br.com.alura.challenges.forum.hub.models.entities.Topic;
import br.com.alura.challenges.forum.hub.models.responses.FindTopicResponse;
import br.com.alura.challenges.forum.hub.repositories.TopicRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FindAllTopicService {

    private final TopicRepository topicRepository;

    private final String COURSE_KEY = "course";
    private final String CREATION_YEAR_KEY = "creation-year";

    public FindAllTopicService(final TopicRepository topicRepository) {
        this.topicRepository = topicRepository;
    }

    public Page<FindTopicResponse> execute(Map<String, String> params, Pageable pageable) {
        Page<FindTopicResponse> result = new PageImpl<>(List.of());
        var sort = pageable.getSort().stream().toList();
        var sortProp = "";

        if (!sort.isEmpty()) {
            sortProp = sort.get(0).getProperty();
        }

        if (!sortProp.equals("creationDate") && params.isEmpty()) {
            result = parseToPageResponse(topicRepository.findAll(pageable));
        } else {
            if (params.isEmpty()) {
                result = findTop10ByOrderByCreationDate();
            } else if (params.containsKey(COURSE_KEY)) {
                result = findByCourseName(params, pageable);
            } else if (params.containsKey(CREATION_YEAR_KEY)) {
                result = findByCreationDateYear(params);
            }
        }

        if (result.isEmpty()) {
            throw new NotFoundException(
                "Não foram encontrados tópicos com as especificações indicadas."
            );
        }

        return result;
    }

    private Page<FindTopicResponse> findByCourseName(Map<String, String> params, Pageable pageable) {
        final var result = topicRepository.findByCourse_Name(params.get(COURSE_KEY), pageable);
        return parseToPageResponse(result);
    }

    private Page<FindTopicResponse> findByCreationDateYear(Map<String, String> params) {
        final var year = Integer.parseInt(params.get(CREATION_YEAR_KEY));
        final var result = topicRepository.findAllByCreationDateYear(year);
        return parseToPageResponse(result);
    }

    private Page<FindTopicResponse> findTop10ByOrderByCreationDate() {
        final var result = topicRepository.findTop10ByOrderByCreationDateAsc();
        return parseToPageResponse(result);
    }

    private Page<FindTopicResponse> parseToPageResponse(final List<Topic> topics) {
        final var result = topics.stream()
                .map(FindTopicResponse::parse)
                .collect(Collectors.toList());
        return new PageImpl<>(result);
    }

    private Page<FindTopicResponse> parseToPageResponse(final Page<Topic> topics) {
        final var result = topics.stream()
                .map(FindTopicResponse::parse)
                .collect(Collectors.toList());
        return new PageImpl<>(result);
    }

}
