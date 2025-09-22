package org.example.unisystem.end_to_end_test.professor_course_integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.unisystem.dto.course.CourseCreateDTO;
import org.example.unisystem.dto.professor.ProfessorCreateDTO;
import org.example.unisystem.end_to_end_test.container.ContainerConfig;
import org.example.unisystem.jpa_repo.CourseJpaRepository;
import org.example.unisystem.jpa_repo.ProfessorJpaRepository;
import org.example.unisystem.jpa_repo.StudentJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@Import(ContainerConfig.class)
public class ProfessorAndCourseIntegrationTest {
    @Autowired
    WebApplicationContext context;

    @Autowired
    ObjectMapper objectMapper;

    private MockMvc mockMvc;

    @Autowired
    ProfessorJpaRepository professorJpaRepository;

    @Autowired
    CourseJpaRepository courseJpaRepository;

    @Autowired
    StudentJpaRepository studentJpaRepository;

    @BeforeEach
    void setMockMvc() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        studentJpaRepository.deleteAll();
        courseJpaRepository.deleteAll();
        professorJpaRepository.deleteAll();
    }

    @Test
    void createCourseByProfessor() throws Exception {
        ProfessorCreateDTO professorCreateDTO = new ProfessorCreateDTO(
                "name", "surname", "department", null
        );

        String request = objectMapper.writeValueAsString(professorCreateDTO);

        String professorResponse = mockMvc.perform(post("/uni/professors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long professorId = objectMapper.readTree(professorResponse).get("id").asLong();

        CourseCreateDTO courseCreateDTO = new CourseCreateDTO(
                "title", 10, null, null, null
        );

        String request2 = objectMapper.writeValueAsString(courseCreateDTO);

        mockMvc.perform(post("/uni/professors/" + professorId + "/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request2))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.title").value(courseCreateDTO.getTitle()))
                .andExpect(jsonPath("$.credits").value(courseCreateDTO.getCredits()))
                .andExpect(jsonPath("$.professor.id").value(professorId))
                .andExpect(jsonPath("$.professor.name").value(professorCreateDTO.getName()))
                .andExpect(jsonPath("$.professor.surname").value(professorCreateDTO.getSurname()))
                .andExpect(jsonPath("$.professor.department").value(professorCreateDTO.getDepartment()));
    }
}
