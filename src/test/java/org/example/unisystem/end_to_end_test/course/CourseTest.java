package org.example.unisystem.end_to_end_test.course;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.unisystem.dto.course.CourseCreateDTO;
import org.example.unisystem.dto.course.CourseDTO;
import org.example.unisystem.dto.course.CoursePatchDTO;
import org.example.unisystem.dto.course.CourseUpdateDTO;
import org.example.unisystem.end_to_end_test.container.ContainerConfig;
import org.example.unisystem.jpa_repo.CourseJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@Import(ContainerConfig.class)
public class CourseTest {
    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    WebApplicationContext context;

    private MockMvc mockMvc;

    @Autowired
    CourseJpaRepository courseJpaRepository;

    @BeforeEach
    void setupMvc() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        courseJpaRepository.deleteAll();
    }

    @Test
    void getCourseById() throws Exception {
        CourseCreateDTO createRequest = new CourseCreateDTO(
                "Title", 10, null, null, null
        );

        String request = objectMapper.writeValueAsString(createRequest);

        String response = mockMvc.perform(post("/uni/courses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request)).andReturn().getResponse().getContentAsString();

        CourseDTO dto = objectMapper.readValue(response, CourseDTO.class);

        mockMvc.perform(get("/uni/courses/" + dto.getId()))
                .andExpect(jsonPath("$.id").value(dto.getId()))
                .andExpect(jsonPath("$.title").value(dto.getTitle()))
                .andExpect(jsonPath("$.credits").value(dto.getCredits()));
    }

    @Test
    void getAllCourses() throws Exception {
        CourseCreateDTO createRequest = new CourseCreateDTO(
                "Title", 10, null, null, null
        );

        String request = objectMapper.writeValueAsString(createRequest);

        String response = mockMvc.perform(post("/uni/courses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request)).andReturn().getResponse().getContentAsString();

        CourseDTO dto = objectMapper.readValue(response, CourseDTO.class);

        CourseCreateDTO createRequest2 = new CourseCreateDTO(
                "Title2", 20, null, null, null
        );

        String request2 = objectMapper.writeValueAsString(createRequest2);

        String response2 = mockMvc.perform(post("/uni/courses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request2)).andReturn().getResponse().getContentAsString();

        CourseDTO dto2 = objectMapper.readValue(response2, CourseDTO.class);

        mockMvc.perform(get("/uni/courses"))
                .andExpect(jsonPath("$[0].id").value(dto.getId()))
                .andExpect(jsonPath("$[0].title").value(dto.getTitle()))
                .andExpect(jsonPath("$[0].credits").value(dto.getCredits()))
                .andExpect(jsonPath("$[1].id").value(dto2.getId()))
                .andExpect(jsonPath("$[1].title").value(dto2.getTitle()))
                .andExpect(jsonPath("$[1].credits").value(dto2.getCredits()));



    }

    @Test
    void createCourse() throws Exception {
        CourseCreateDTO createRequest = new CourseCreateDTO(
                "Title", 10, null, null, null
        );

        String request = objectMapper.writeValueAsString(createRequest);

        String response = mockMvc.perform(post("/uni/courses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request)).andReturn().getResponse().getContentAsString();

        CourseDTO dto = objectMapper.readValue(response, CourseDTO.class);

        assertEquals(createRequest.getTitle(), dto.getTitle());
        assertEquals(createRequest.getTitle(), dto.getTitle());
        assertEquals(1L, dto.getId());
    }

    @Test
    void updateCourse() throws Exception {
        CourseDTO courseDTO = new CourseDTO(
                1L, "Title", 10, null, null, null
        );

        CourseUpdateDTO updateDTO = new CourseUpdateDTO(
                "Title2", 20, null, null, null
        );

        String updateRequest = objectMapper.writeValueAsString(updateDTO);
        String request = objectMapper.writeValueAsString(courseDTO);

        String response = mockMvc.perform(post("/uni/courses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request)).andReturn().getResponse().getContentAsString();

        mockMvc.perform(put("/uni/courses/" + courseDTO.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateRequest))
                .andExpect(jsonPath("$.id").value(courseDTO.getId()))
                .andExpect(jsonPath("$.title").value(updateDTO.getTitle()))
                .andExpect(jsonPath("$.credits").value(updateDTO.getCredits()));
    }

    @Test
    void patchCourse() throws Exception {
        CourseCreateDTO createRequest = new CourseCreateDTO(
                "Title", 10, null, null, null
        );

        String request = objectMapper.writeValueAsString(createRequest);

        String response = mockMvc.perform(post("/uni/courses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request)).andReturn().getResponse().getContentAsString();

        CourseDTO dto = objectMapper.readValue(response, CourseDTO.class);

        CoursePatchDTO patchDTO = new CoursePatchDTO(
                "Title2", 20, null, null, null
        );

        String patchRequest = objectMapper.writeValueAsString(patchDTO);
        mockMvc.perform(patch("/uni/courses/" + dto.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(patchRequest))
                .andExpect(jsonPath("$.id").value(dto.getId()))
                .andExpect(jsonPath("$.title").value(patchDTO.getTitle()))
                .andExpect(jsonPath("$.credits").value(patchDTO.getCredits()));


    }

    @Test
    void deleteCourse() throws Exception {
        CourseCreateDTO createRequest = new CourseCreateDTO(
                "Title", 10, null, null, null
        );

        String request = objectMapper.writeValueAsString(createRequest);

        String response = mockMvc.perform(post("/uni/courses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request)).andReturn().getResponse().getContentAsString();

        CourseDTO dto = objectMapper.readValue(response, CourseDTO.class);

        mockMvc.perform(delete("/uni/courses/" + dto.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/uni/courses/" + dto.getId()))
                .andExpect(status().isNotFound());
    }
}
