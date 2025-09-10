package org.example.unisystem.jpa_repo;

import org.example.unisystem.entity.Student;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface StudentJpaRepository extends JpaRepository<Student, Long> {

    @EntityGraph(attributePaths = {"courses", "submissions"})
    @Query("select s from Student s where s.id = :id")
    Optional<Student> findByIdGraph(Long id);

}
