package br.com.alura.challenges.forum.hub.repositories;

import br.com.alura.challenges.forum.hub.models.entities.Topic;
import br.com.alura.challenges.forum.hub.models.transfers.SimpleFindTopicTransfer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TopicRepository extends JpaRepository<Topic, Long> {

    Page<Topic> findByCourse_Name(String courseName, Pageable pageable);

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

    @Query("SELECT t FROM Topic t WHERE YEAR(t.creationDate) = :year")
    List<Topic> findAllByCreationDateYear(Integer year);

    @Modifying
    @Query("UPDATE Topic t SET t.author.id = NULL WHERE t.author.id = :authorId")
    void deactivateAuthorById(Long authorId);

}
