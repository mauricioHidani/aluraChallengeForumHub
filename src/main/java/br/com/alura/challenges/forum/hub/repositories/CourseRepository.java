package br.com.alura.challenges.forum.hub.repositories;

import br.com.alura.challenges.forum.hub.models.entities.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    Boolean existsByName(String name);

}
