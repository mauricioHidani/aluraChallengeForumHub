package br.com.alura.challenges.forum.hub.repositories;

import br.com.alura.challenges.forum.hub.models.entities.Response;
import br.com.alura.challenges.forum.hub.models.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResponseRepository extends JpaRepository<Response, Long> {

    List<Response> findByAuthor(User author);

    @Modifying
    @Query("UPDATE Response r SET r.author.id = NULL WHERE r.author.id = :authorId")
    void deactivateAuthorById(Long authorId);

}
