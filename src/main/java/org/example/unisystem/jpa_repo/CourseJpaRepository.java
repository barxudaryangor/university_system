package org.example.unisystem.jpa_repo;

import org.example.unisystem.entity.Course;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CourseJpaRepository extends JpaRepository<Course, Long> {
    @EntityGraph(attributePaths = {"students", "professor", "assignments"})
    @Query("select c from Course c where c.id = :id")
    Optional<Course> findByIdGraph(Long id);
}
