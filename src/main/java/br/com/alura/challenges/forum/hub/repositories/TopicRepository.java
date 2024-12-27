package br.com.alura.challenges.forum.hub.repositories;

import br.com.alura.challenges.forum.hub.models.entities.Topic;
import br.com.alura.challenges.forum.hub.models.transfers.SimpleFindTopicTransfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TopicRepository extends JpaRepository<Topic, UUID> {

    List<Topic> findTop10ByOrderByCreationDateAsc();

    Boolean existsByTitleAndMessage(String title, String message);

    @Query("""
            SELECT
            t.id AS id,
            t.title AS title,
            t.message AS message,
            t.creationDate AS creationDate,
            t.status AS status,
            a.id AS authorId,
            a.name AS authorName,
            c.id AS courseId,
            c.name AS courseName
            FROM Topic t
            JOIN t.author a
            JOIN t.course c
            WHERE t.id = :topicId
            """)
    SimpleFindTopicTransfer findTopicDetailsById(Long topicId);

}
