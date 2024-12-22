package br.com.alura.challenges.forum.hub.repositories;

import br.com.alura.challenges.forum.hub.models.entities.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TopicRepository extends JpaRepository<Topic, UUID> {

}
