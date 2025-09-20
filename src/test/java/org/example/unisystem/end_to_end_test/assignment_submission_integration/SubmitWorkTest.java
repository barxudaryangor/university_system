package org.example.unisystem.end_to_end_test.assignment_submission_integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.unisystem.dto.assignment.AssignmentCreateDTO;
import org.example.unisystem.dto.assignment.AssignmentDTO;
import org.example.unisystem.dto.course.CourseCreateDTO;
import org.example.unisystem.dto.course.CourseDTO;
import org.example.unisystem.dto.student.StudentCreateDTO;
import org.example.unisystem.dto.student.StudentDTO;
import org.example.unisystem.dto.submission.SubmissionCreateDTO;
import org.example.unisystem.end_to_end_test.container.ContainerConfig;
import org.example.unisystem.enums.Gender;
import org.example.unisystem.jpa_repo.AssignmentJpaRepository;
import org.example.unisystem.jpa_repo.StudentJpaRepository;
import org.example.unisystem.jpa_repo.SubmissionJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@Import(ContainerConfig.class)
public class SubmitWorkTest {

    @Autowired
    WebApplicationContext context;

    @Autowired
    ObjectMapper objectMapper;

    private MockMvc mockMvc;

    @Autowired
    StudentJpaRepository studentJpaRepository;

    @Autowired
    AssignmentJpaRepository assignmentJpaRepository;

    @Autowired
    SubmissionJpaRepository submissionJpaRepository;

    @BeforeEach
    void setMockmvc() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        studentJpaRepository.deleteAll();
        assignmentJpaRepository.deleteAll();
        submissionJpaRepository.deleteAll();
    }

    @Test
    void submitWork() throws Exception {
        StudentCreateDTO studentCreateDTO = new StudentCreateDTO(
                "name", "surname", Gender.MALE,
                LocalDate.of(2020, 10, 10),
                LocalDate.of(2020, 12, 12),
                "gor@gmail.com",
                null, null
        );

        String requestStudent = objectMapper.writeValueAsString(studentCreateDTO);

        String studentResponse = mockMvc.perform(post("/uni/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestStudent))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        StudentDTO savedStudent = objectMapper.readValue(studentResponse, StudentDTO.class);

        CourseCreateDTO courseCreateDTO = new CourseCreateDTO("Course 1", 5, null, null, null);
        String requestCourse = objectMapper.writeValueAsString(courseCreateDTO);

        String courseResponse = mockMvc.perform(post("/uni/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestCourse))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        CourseDTO savedCourse = objectMapper.readValue(courseResponse, CourseDTO.class);

        AssignmentCreateDTO assignmentCreateDTO = new AssignmentCreateDTO(
                "title",
                LocalDate.of(2030, 10, 10),
                null,
                null
        );

        String requestAssignment = objectMapper.writeValueAsString(assignmentCreateDTO);

        String assignmentResponse = mockMvc.perform(post("/uni/assignments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestAssignment))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        AssignmentDTO savedAssignment = objectMapper.readValue(assignmentResponse, AssignmentDTO.class);


        SubmissionCreateDTO submissionCreateDTO = new SubmissionCreateDTO(
                LocalDate.of(2020, 10, 10),
                new BigDecimal(10),
                null, null
        );

        String requestSubmission = objectMapper.writeValueAsString(submissionCreateDTO);

        mockMvc.perform(post("/uni/students/{studentId}/assignments/{assignmentId}/submissions",
                        savedStudent.getId(), savedAssignment.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestSubmission))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.submittedAt").value(submissionCreateDTO.getSubmittedAt().toString()))
                .andExpect(jsonPath("$.grade").value(submissionCreateDTO.getGrade()))
                .andExpect(jsonPath("$.student.id").value(savedStudent.getId()))
                .andExpect(jsonPath("$.student.name").value(savedStudent.getName()))
                .andExpect(jsonPath("$.student.surname").value(savedStudent.getSurname()))
                .andExpect(jsonPath("$.assignment.id").value(savedAssignment.getId()))
                .andExpect(jsonPath("$.assignment.title").value(savedAssignment.getTitle()));
    }

}
