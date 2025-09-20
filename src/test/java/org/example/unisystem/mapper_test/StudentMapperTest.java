package org.example.unisystem.mapper_test;

import org.example.unisystem.dto.student.StudentDTO;
import org.example.unisystem.dto.student.StudentCreateDTO;
import org.example.unisystem.entity.Course;
import org.example.unisystem.entity.Student;
import org.example.unisystem.entity.Submission;
import org.example.unisystem.enums.Gender;
import org.example.unisystem.jpa_repo.CourseJpaRepository;
import org.example.unisystem.jpa_repo.SubmissionJpaRepository;
import org.example.unisystem.mappers.StudentMapper;
import org.example.unisystem.short_dto.CourseShortDTO;
import org.example.unisystem.short_dto.SubmissionShortDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
public class StudentMapperTest {

    @Autowired
    StudentMapper studentMapper;

    @Autowired
    CourseJpaRepository courseJpaRepository;

    @Autowired
    SubmissionJpaRepository submissionJpaRepository;

    @Test
    void studentToDTO() {
        Course course = new Course(1L, "title", 10, null, null, null);
        Submission submission = new Submission(2L, LocalDate.now(), new BigDecimal(20), null, null);

        Student student = new Student(3L, "name", "surname", Gender.FEMALE, LocalDate.now(), LocalDate.now(), "email", Set.of(course), Set.of(submission));

        StudentDTO studentDTO = studentMapper.studentToDTO(student);

        assertEquals(3L, studentDTO.getId());
        assertEquals(student.getBirthdate(), studentDTO.getBirthdate());
        assertEquals(student.getEmail(), studentDTO.getEmail());
        assertEquals(student.getName(), studentDTO.getName());
        assertEquals(student.getSurname(), studentDTO.getSurname());
        assertEquals(student.getSex(), studentDTO.getSex());
        assertEquals(student.getEnrolmentDate(), studentDTO.getEnrolmentDate());
        assertEquals(student.getSubmissions().iterator().next().getId(), studentDTO.getSubmissions().get(0).id());
        assertEquals(student.getCourses().iterator().next().getId(), studentDTO.getCourses().get(0).id());
    }

    @Test
    void dtoToStudent() {
        Course course = new Course(null, "title", 10, null, null, null);
        courseJpaRepository.save(course);

        CourseShortDTO courseDTO = new CourseShortDTO(
                course.getId(), course.getTitle(), course.getCredits()
        );

        Submission submission = new Submission(null, LocalDate.now(), new BigDecimal(20), null, null);
        submissionJpaRepository.save(submission);

        SubmissionShortDTO submissionDTO = new SubmissionShortDTO(
                submission.getId(), submission.getSubmittedAt(), submission.getGrade()
        );



        StudentCreateDTO studentDTO  = new StudentCreateDTO(
                "name", "surname", Gender.FEMALE, LocalDate.now(), LocalDate.now(), "email", List.of(courseDTO), List.of(submissionDTO)
        );

        Student student = studentMapper.dtoToStudent(studentDTO);

        assertEquals(studentDTO.getName(), student.getName());
        assertEquals(studentDTO.getSurname(), student.getSurname());
        assertEquals(studentDTO.getSex(), student.getSex());
        assertEquals(studentDTO.getBirthdate(), student.getBirthdate());
        assertEquals(studentDTO.getEnrolmentDate(), student.getEnrolmentDate());
        assertEquals(studentDTO.getEmail(), student.getEmail());
        assertEquals(studentDTO.getCourses().get(0).id(), student.getCourses().iterator().next().getId());
        assertEquals(studentDTO.getSubmissions().get(0).id(), student.getSubmissions().iterator().next().getId());

    }


}
