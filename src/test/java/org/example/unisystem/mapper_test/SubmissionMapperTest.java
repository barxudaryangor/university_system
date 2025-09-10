package org.example.unisystem.mapper_test;

import org.example.unisystem.dto.submission.SubmissionCreateDTO;
import org.example.unisystem.dto.submission.SubmissionDTO;
import org.example.unisystem.entity.Assignment;
import org.example.unisystem.entity.Student;
import org.example.unisystem.entity.Submission;
import org.example.unisystem.enums.Gender;
import org.example.unisystem.jpa_repo.AssignmentJpaRepository;
import org.example.unisystem.jpa_repo.StudentJpaRepository;
import org.example.unisystem.mappers.SubmissionMapper;
import org.example.unisystem.short_dto.AssignmentShortDTO;
import org.example.unisystem.short_dto.StudentShortDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class SubmissionMapperTest {

    @Autowired
    SubmissionMapper submissionMapper;

    @Autowired
    StudentJpaRepository studentJpaRepository;

    @Autowired
    AssignmentJpaRepository assignmentJpaRepository;

    @Test
    void submissionToDTO() {
        Student student = new Student(
                1L, "name", "surname", Gender.Female, LocalDate.now(),LocalDate.now(), "email", null, null
        );

        Assignment assignment = new Assignment(2L, "title", LocalDate.now(), null, null);

        Submission submission = new Submission(3L, LocalDate.now(), new BigDecimal(10), student, assignment);

        SubmissionDTO submissionDTO = submissionMapper.submissionToDTO(submission);

        assertEquals(submission.getId(), submissionDTO.getId());
        assertEquals(submission.getGrade(), submissionDTO.getGrade());
        assertEquals(submission.getSubmittedAt(), submissionDTO.getSubmittedAt());
        assertEquals(submission.getStudent().getId(), submissionDTO.getStudent().id());
        assertEquals(submission.getAssignment().getId(), submissionDTO.getAssignment().id());

    }

    @Test
    void dtoToSubmission() {
        Student student = new Student(
                null, "name", "surname", Gender.Female, LocalDate.now(),LocalDate.now(), "email", null, null
        );
        studentJpaRepository.save(student);

        StudentShortDTO studentDTO = new StudentShortDTO(
                student.getId(), student.getName(), student.getSurname(), student.getSex(), student.getBirthdate(), student.getEnrolmentDate(), student.getEmail()
        );


        Assignment assignment = new Assignment(null, "title", LocalDate.now(), null, null);
        assignmentJpaRepository.save(assignment);

        AssignmentShortDTO assignmentDTO = new AssignmentShortDTO(
                assignment.getId(), assignment.getTitle(), assignment.getDueDate()
        );

        SubmissionCreateDTO submissionDTO = new SubmissionCreateDTO(
                LocalDate.now(), new BigDecimal(10), studentDTO, assignmentDTO
        );

        Submission submission = submissionMapper.dtoToSubmission(submissionDTO);

        assertEquals(submission.getGrade(), submissionDTO.getGrade());
        assertEquals(submission.getSubmittedAt(), submissionDTO.getSubmittedAt());
        assertEquals(submission.getStudent().getId(), submissionDTO.getStudent().id());
        assertEquals(submission.getAssignment().getId(), submissionDTO.getAssignment().id());
    }
}
