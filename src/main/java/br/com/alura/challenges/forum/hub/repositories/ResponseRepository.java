package br.com.alura.challenges.forum.hub.repositories;

import br.com.alura.challenges.forum.hub.models.entities.Response;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ResponseRepository extends JpaRepository<Response, UUID> {

    @Query("SELECT r FROM Response r WHERE r.topic.id = :topicId")
    Optional<Response> findByTopicId(UUID topicId);

}
