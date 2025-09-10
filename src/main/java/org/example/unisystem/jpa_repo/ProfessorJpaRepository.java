package org.example.unisystem.jpa_repo;

import org.example.unisystem.entity.Professor;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ProfessorJpaRepository extends JpaRepository<Professor, Long> {
    @EntityGraph(attributePaths = {"courses"})
    @Query("select p from Professor p where p.id = :id")
    Optional<Professor> findByIdGraph(Long id);
}
