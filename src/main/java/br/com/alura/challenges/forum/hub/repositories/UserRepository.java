package br.com.alura.challenges.forum.hub.repositories;

import br.com.alura.challenges.forum.hub.models.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Boolean existsByEmail(String email);

    @Modifying
    @Query(value = "DELETE FROM perfis_de_usuarios WHERE usuario_id = :id",
            nativeQuery = true)
    void deleteUserRolesAssociated(Long id);

}
