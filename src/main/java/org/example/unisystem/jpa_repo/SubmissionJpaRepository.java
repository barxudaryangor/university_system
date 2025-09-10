package org.example.unisystem.jpa_repo;

import org.example.unisystem.entity.Submission;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface SubmissionJpaRepository extends JpaRepository<Submission, Long> {

    @EntityGraph(attributePaths = {"student", "assignment"})
    @Query("select s from Submission s where s.id = :id")
    Optional<Submission> findByIdGraph(Long id);
}

