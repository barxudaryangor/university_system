package org.example.unisystem.end_to_end_test.course_assignment_integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.unisystem.dto.assignment.AssignmentCreateDTO;
import org.example.unisystem.dto.course.CourseCreateDTO;
import org.example.unisystem.dto.professor.ProfessorCreateDTO;
import org.example.unisystem.end_to_end_test.container.ContainerConfig;
import org.example.unisystem.jpa_repo.AssignmentJpaRepository;
import org.example.unisystem.jpa_repo.CourseJpaRepository;
import org.example.unisystem.jpa_repo.ProfessorJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@Import(ContainerConfig.class)

public class CourseAndAssignmentIntegrationTest {
    @Autowired
    WebApplicationContext context;

    @Autowired
    ObjectMapper objectMapper;

    private MockMvc mockMvc;

    @Autowired
    CourseJpaRepository courseJpaRepository;

    @Autowired
    ProfessorJpaRepository professorJpaRepository;

    @Autowired
    AssignmentJpaRepository assignmentJpaRepository;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setMockMvc() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        jdbcTemplate.execute("""
        TRUNCATE TABLE student_course, submission, assignment, student, course, professor 
        RESTART IDENTITY CASCADE
    """);
    }

    @Test
    void createAssignmentForCourseWithoutProfessor() throws Exception {
        AssignmentCreateDTO assignmentCreateDTO = new AssignmentCreateDTO(
                "title", LocalDate.of(2030,10,10), null, null
        );

        String request = objectMapper.writeValueAsString(assignmentCreateDTO);

        CourseCreateDTO courseCreateDTO = new CourseCreateDTO(
                "title", 10, null, null,null
        );

        String request2 = objectMapper.writeValueAsString(courseCreateDTO);

        mockMvc.perform(post("/uni/courses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request2));

        mockMvc.perform(post("/uni/courses/" + 1L + "/assignments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value(assignmentCreateDTO.getTitle()))
                .andExpect(jsonPath("$.dueDate").value(assignmentCreateDTO.getDueDate().toString()))
                .andExpect(jsonPath("$.course.id").value(1L))
                .andExpect(jsonPath("$.course.title").value(courseCreateDTO.getTitle()))
                .andExpect(jsonPath("$.course.credits").value(courseCreateDTO.getCredits()));
    }


    @Test
    void createAssignmentForCourse() throws Exception {
        AssignmentCreateDTO assignmentCreateDTO = new AssignmentCreateDTO(
                "title", LocalDate.of(2030,10,10), null, null
        );

        String request = objectMapper.writeValueAsString(assignmentCreateDTO);

        ProfessorCreateDTO professorCreateDTO = new ProfessorCreateDTO(
                "name", "surname", "department", null
        );

        String request3 = objectMapper.writeValueAsString(professorCreateDTO);

        mockMvc.perform(post("/uni/professors")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request3));

        CourseCreateDTO courseCreateDTO = new CourseCreateDTO(
                "title", 10, null, null,null
        );

        String request2 = objectMapper.writeValueAsString(courseCreateDTO);

        mockMvc.perform(post("/uni/professors/{professorId}/courses", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request2))
                .andExpect(status().isCreated());


        mockMvc.perform(post("/uni/professors/{professorId}/courses/{courseId}/assignments", 1L, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value(assignmentCreateDTO.getTitle()))
                .andExpect(jsonPath("$.dueDate").value(assignmentCreateDTO.getDueDate().toString()))
                .andExpect(jsonPath("$.course.id").value(1L))
                .andExpect(jsonPath("$.course.title").value(courseCreateDTO.getTitle()))
                .andExpect(jsonPath("$.course.credits").value(courseCreateDTO.getCredits()));
    }
}
