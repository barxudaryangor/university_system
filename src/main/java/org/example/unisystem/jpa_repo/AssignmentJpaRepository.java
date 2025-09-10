package org.example.unisystem.jpa_repo;

import org.example.unisystem.entity.Assignment;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AssignmentJpaRepository extends JpaRepository<Assignment, Long> {

    @EntityGraph(attributePaths = {"course", "submissions"})
    @Query("select a from Assignment a where a.id = :id")
    Optional<Assignment> findByIdGraph(Long id);
}
