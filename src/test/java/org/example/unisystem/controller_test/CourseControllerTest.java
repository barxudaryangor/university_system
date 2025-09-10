package org.example.unisystem.controller_test;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.unisystem.controller.CourseController;
import org.example.unisystem.dto.course.CourseCreateDTO;
import org.example.unisystem.dto.course.CourseDTO;
import org.example.unisystem.dto.course.CoursePatchDTO;
import org.example.unisystem.dto.course.CourseUpdateDTO;
import org.example.unisystem.service_interface.CourseService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CourseController.class)
public class CourseControllerTest {

    @MockitoBean
    CourseService courseService;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;

    @Test
    void getCourseById() throws Exception {
        CourseDTO courseDTO = new CourseDTO(
                1L, "Title", 10, null, null, null
        );

        when(courseService.getCourseById(1L)).thenReturn(courseDTO);

        mockMvc.perform(get("/uni/courses/" + courseDTO.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(courseDTO.getId()))
                .andExpect(jsonPath("$.title").value(courseDTO.getTitle()))
                .andExpect(jsonPath("$.credits").value(courseDTO.getCredits()));

        verify(courseService).getCourseById(1L);

    }

    @Test
    void getAllCourses() throws Exception {
        CourseDTO courseDTO = new CourseDTO(
                1L, "Title", 10, null, null, null
        );

        CourseDTO courseDTO2 = new CourseDTO(
                2L, "Title2", 10, null, null,null
        );

        when(courseService.getAllCourses()).thenReturn(List.of(courseDTO, courseDTO2));

        mockMvc.perform(get("/uni/courses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(courseDTO.getId()))
                .andExpect(jsonPath("$[0].title").value(courseDTO.getTitle()))
                .andExpect(jsonPath("$[0].credits").value(courseDTO.getCredits()))
                .andExpect(jsonPath("$[1].id").value(courseDTO2.getId()))
                .andExpect(jsonPath("$[1].title").value(courseDTO2.getTitle()))
                .andExpect(jsonPath("$[1].credits").value(courseDTO2.getCredits()));

        verify(courseService).getAllCourses();
    }

    @Test
    void createCourse() throws Exception {

        CourseCreateDTO createDTO = new CourseCreateDTO(
                "Title", 10, null, null, null
        );

        CourseDTO courseDTO = new CourseDTO(
                1L, "Title", 10, null, null, null
        );

        when(courseService.createCourse(any(CourseCreateDTO.class))).thenReturn(courseDTO);


        String request = objectMapper.writeValueAsString(createDTO);

        mockMvc.perform(post("/uni/courses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(courseDTO.getId()))
                .andExpect(jsonPath("$.title").value(courseDTO.getTitle()))
                .andExpect(jsonPath("$.credits").value(courseDTO.getCredits()));

        verify(courseService).createCourse(any(CourseCreateDTO.class));

    }

    @Test
    void updateCourse() throws Exception {
        CourseDTO courseDTO = new CourseDTO(
                1L, "Title", 10, null, null, null
        );

        CourseUpdateDTO updateDTO = new CourseUpdateDTO(
                "Title2", 20, null, null, null
        );

        CourseDTO updatedCourse = new CourseDTO(
                1L, "Title2", 20, null, null, null
        );

        String request = objectMapper.writeValueAsString(updateDTO);

        when(courseService.updateCourse(eq(1L), any(CourseUpdateDTO.class))).thenReturn(updatedCourse);

        mockMvc.perform(put("/uni/courses/"+courseDTO.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(request))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(updatedCourse.getId()))
                .andExpect(jsonPath("$.title").value(updatedCourse.getTitle()))
                .andExpect(jsonPath("$.credits").value(updatedCourse.getCredits()));

        verify(courseService).updateCourse(any(Long.class), any(CourseUpdateDTO.class));
    }

    @Test
    void patchCourse() throws Exception {
        CourseDTO courseDTO = new CourseDTO(
                1L, "Title", 10, null, null, null
        );

        CoursePatchDTO patchDTO = new CoursePatchDTO(
                "Title2", 15, null, null, null
        );

        doAnswer(invocation -> {
            Long id = invocation.getArgument(0);
            CoursePatchDTO dto = invocation.getArgument(1);
            return new CourseDTO(
                    id, dto.getTitle(), dto.getCredits(), null, null, null
            );
        }).when(courseService).patchCourse(any(Long.class), any(CoursePatchDTO.class));

        String request = objectMapper.writeValueAsString(patchDTO);

        mockMvc.perform(patch("/uni/courses/" + courseDTO.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(patchDTO.getTitle()))
                .andExpect(jsonPath("$.credits").value(patchDTO.getCredits()));

        verify(courseService).patchCourse(eq(courseDTO.getId()), any(CoursePatchDTO.class));
    }

    @Test
    void deleteCourse() throws Exception {
        CourseDTO dto = new CourseDTO(
                1L, "Title", 10, null, null, null
        );

        doNothing().when(courseService).deleteCourse(dto.getId());

        mockMvc.perform(delete("/uni/courses/" + dto.getId()))
                .andExpect(status().isNoContent());

        verify(courseService).deleteCourse(dto.getId());
    }
}
