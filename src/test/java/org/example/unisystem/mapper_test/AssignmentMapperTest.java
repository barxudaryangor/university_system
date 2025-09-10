package org.example.unisystem.mapper_test;

import jakarta.transaction.Transactional;
import org.example.unisystem.dto.assignment.AssignmentCreateDTO;
import org.example.unisystem.dto.assignment.AssignmentDTO;
import org.example.unisystem.entity.Assignment;
import org.example.unisystem.entity.Course;
import org.example.unisystem.entity.Submission;
import org.example.unisystem.jpa_repo.CourseJpaRepository;
import org.example.unisystem.jpa_repo.SubmissionJpaRepository;
import org.example.unisystem.mappers.AssignmentMapper;
import org.example.unisystem.short_dto.CourseShortDTO;
import org.example.unisystem.short_dto.SubmissionShortDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional

public class AssignmentMapperTest {
    @Autowired
    AssignmentMapper assignmentMapper;

    @Autowired
    CourseJpaRepository courseJpaRepository;

    @Autowired
    SubmissionJpaRepository submissionJpaRepository;

    @Test
    void testAssignmentToDTO() {
        Course course = new Course(
                1L, "Course", 10, null, null, null
        );

        Submission submission = new Submission(
                2L, LocalDate.now(), new BigDecimal(3.25), null, null
        );

        Assignment assignment = new Assignment();
        assignment.setId(3L);
        assignment.setTitle("Assignment");
        assignment.setDueDate(LocalDate.now());
        assignment.setCourse(course);
        assignment.setSubmissions(Set.of(submission));

        AssignmentDTO dto = assignmentMapper.assignmentToDTO(assignment);

        assertEquals(3L, dto.getId());
        assertEquals("Assignment", dto.getTitle());
        assertEquals(LocalDate.now(), dto.getDueDate());
        assertEquals(1L, dto.getCourse().id());
        assertEquals(2L, dto.getSubmissions().get(0).id());
    }

    @Test
    void dtoToAssignment() {
        Course course = new Course(null, "Title", 10, null, null, null);
        courseJpaRepository.save(course);

        Submission submission = new Submission(null, LocalDate.now(), new BigDecimal(3), null, null);
        submissionJpaRepository.save(submission);

        submissionJpaRepository.flush();
        courseJpaRepository.flush();
        assertTrue(submissionJpaRepository.findById(submission.getId()).isPresent());

        CourseShortDTO courseDTO = new CourseShortDTO(
                course.getId(), course.getTitle(), course.getCredits()
        );

        SubmissionShortDTO submissionDTO = new SubmissionShortDTO(
                submission.getId(), submission.getSubmittedAt(), submission.getGrade()
        );

        LocalDate dueDate = LocalDate.of(2025, 9, 1);

        AssignmentCreateDTO assignmentDTO = new AssignmentCreateDTO(
                "title", dueDate, courseDTO, List.of(submissionDTO)
        );

        Assignment assignment = assignmentMapper.dtoToAssignment(assignmentDTO);

        assertEquals("title", assignment.getTitle());
        assertEquals(dueDate, assignment.getDueDate());
        assertEquals(course.getId(), assignment.getCourse().getId());
        assertEquals(submission.getId(), assignment.getSubmissions().iterator().next().getId());
        assertEquals("Title", assignment.getCourse().getTitle());
    }


}
