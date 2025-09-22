package org.example.unisystem.controller_test;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.unisystem.controller.ProfessorController;
import org.example.unisystem.dto.assignment.AssignmentCreateDTO;
import org.example.unisystem.dto.assignment.AssignmentDTO;
import org.example.unisystem.dto.course.CourseCreateDTO;
import org.example.unisystem.dto.course.CourseDTO;
import org.example.unisystem.dto.professor.ProfessorCreateDTO;
import org.example.unisystem.dto.professor.ProfessorDTO;
import org.example.unisystem.dto.professor.ProfessorPatchDTO;
import org.example.unisystem.dto.professor.ProfessorUpdateDTO;
import org.example.unisystem.pagination.PaginationResponse;
import org.example.unisystem.service_interface.AssignmentService;
import org.example.unisystem.service_interface.CourseService;
import org.example.unisystem.service_interface.ProfessorService;
import org.example.unisystem.short_dto.CourseShortDTO;
import org.example.unisystem.short_dto.ProfessorShortDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProfessorController.class)
@AutoConfigureMockMvc(addFilters = false)
public class ProfessorControllerTest {
    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    ProfessorService professorService;

    @MockitoBean
    AssignmentService assignmentService;

    @MockitoBean
    CourseService courseService;

    @Test
    void getProfessorById() throws Exception {
        ProfessorDTO professorDTO = new ProfessorDTO(
                1L, "Name", "Surname", "Department", null
        );

        when(professorService.getProfessorById(1L)).thenReturn(professorDTO);

        mockMvc.perform(get("/uni/professors/" + professorDTO.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(professorDTO.getId()))
                .andExpect(jsonPath("$.name").value(professorDTO.getName()))
                .andExpect(jsonPath("$.surname").value(professorDTO.getSurname()))
                .andExpect(jsonPath("$.department").value(professorDTO.getDepartment()));

        verify(professorService).getProfessorById(1L);

    }

    @Test
    void getAllProfessors() throws Exception {
        ProfessorDTO professorDTO = new ProfessorDTO(
                1L, "Name", "Surname", "Department", null
        );

        ProfessorDTO professorDTO2 = new ProfessorDTO(
                2L, "Name2", "Surname2", "Department2", null
        );

        PaginationResponse<ProfessorDTO> response = new PaginationResponse<>(
                new PageImpl<>(List.of(professorDTO, professorDTO2),
                        PageRequest.of(0,10), 2)
        );

        when(professorService.getAllProfessors(any(Pageable.class))).thenReturn(response);

        mockMvc.perform(get("/uni/professors?page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(professorDTO.getId()))
                .andExpect(jsonPath("$.content[0].name").value(professorDTO.getName()))
                .andExpect(jsonPath("$.content[0].surname").value(professorDTO.getSurname()))
                .andExpect(jsonPath("$.content[0].department").value(professorDTO.getDepartment()))
                .andExpect(jsonPath("$.content[1].id").value(professorDTO2.getId()))
                .andExpect(jsonPath("$.content[1].name").value(professorDTO2.getName()))
                .andExpect(jsonPath("$.content[1].surname").value(professorDTO2.getSurname()))
                .andExpect(jsonPath("$.content[1].department").value(professorDTO2.getDepartment()))
                .andExpect(jsonPath("$.totalPages").value(response.getTotalPages()))
                .andExpect(jsonPath("$.totalElements").value(response.getTotalElements()))
                .andExpect(jsonPath("$.pageNum").value(response.getPageNum()))
                .andExpect(jsonPath("$.last").value(response.isLast()))
                .andExpect(jsonPath("$.pageNum").value(0));


        verify(professorService).getAllProfessors(any(Pageable.class));
    }

    @Test
    void createProfessor() throws Exception {
        ProfessorCreateDTO createDTO = new ProfessorCreateDTO(
                "Name", "Surname", "Department", null
        );

        ProfessorDTO savedProfessor = new ProfessorDTO(
                1L, "Name", "Surname", "Department", null
        );

        String request = objectMapper.writeValueAsString(createDTO);

        when(professorService.createProfessor(any(ProfessorCreateDTO.class))).thenReturn(savedProfessor);

        mockMvc.perform(post("/uni/professors")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(savedProfessor.getId()))
                .andExpect(jsonPath("$.name").value(savedProfessor.getName()))
                .andExpect(jsonPath("$.surname").value(savedProfessor.getSurname()))
                .andExpect(jsonPath("$.department").value(savedProfessor.getDepartment()));

        verify(professorService).createProfessor(any(ProfessorCreateDTO.class));


    }

    @Test
    void createCourseByProfessor() throws Exception {
        ProfessorShortDTO professorDTO = new ProfessorShortDTO(
                1L, "name", "surname", "department"
        );

        CourseCreateDTO createDTO = new CourseCreateDTO(
                "title", 10, null, null, null
        );

        String request = objectMapper.writeValueAsString(createDTO);

        CourseDTO courseDTO = new CourseDTO(
                2L, "title", 10, null, professorDTO, null
        );

        when(courseService.createCourseByProfessor(eq(1L), any(CourseCreateDTO.class))).thenReturn(courseDTO);

        mockMvc.perform(post("/uni/professors/1/courses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(courseDTO.getId()))
                .andExpect(jsonPath("$.title").value(courseDTO.getTitle()))
                .andExpect(jsonPath("$.credits").value(courseDTO.getCredits()))
                .andExpect(jsonPath("$.professor.id").value(professorDTO.id()))
                .andExpect(jsonPath("$.professor.name").value(professorDTO.name()))
                .andExpect(jsonPath("$.professor.surname").value(professorDTO.surname()))
                .andExpect(jsonPath("$.professor.department").value(professorDTO.department()));

        verify(courseService).createCourseByProfessor(eq(1L), any(CourseCreateDTO.class));
        verifyNoMoreInteractions(courseService);
    }

    @Test
    void createAssignmentForProfessor() throws Exception {
        AssignmentCreateDTO dto = new AssignmentCreateDTO(
                "title", LocalDate.of(2030,10,10), null, null
        );

        String request = objectMapper.writeValueAsString(dto);

        CourseShortDTO courseShortDTO = new CourseShortDTO(
                1L, "t", 10
        );

        ProfessorDTO professorDTO = new ProfessorDTO(
                2L,"name", "surname", "department", List.of(courseShortDTO)
        );

        AssignmentDTO assignmentDTO = new AssignmentDTO(
                3L, "title", LocalDate.of(2030,10,10), courseShortDTO, null
        );

        when(assignmentService.createAssignmentForCourse(eq(2L), eq(1L), any(AssignmentCreateDTO.class)))
                .thenReturn(assignmentDTO);

        mockMvc.perform(post("/uni/professors/{professorId}/courses/{courseId}/assignments", 2L, 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(request))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(assignmentDTO.getId()))
                .andExpect(jsonPath("$.title").value(assignmentDTO.getTitle()))
                .andExpect(jsonPath("$.dueDate").value(assignmentDTO.getDueDate().toString()))
                .andExpect(jsonPath("$.course.id").value(courseShortDTO.id()))
                .andExpect(jsonPath("$.course.title").value(courseShortDTO.title()))
                .andExpect(jsonPath("$.course.credits").value(courseShortDTO.credits()));

        verify(assignmentService).createAssignmentForCourse(eq(2L), eq(1L), any(AssignmentCreateDTO.class));
        verifyNoMoreInteractions(assignmentService);
    }

    @Test
    void updateProfessor() throws Exception {
        ProfessorDTO professorDTO = new ProfessorDTO(
                1L, "Name", "Surname", "department", null
        );

        ProfessorUpdateDTO updateDTO = new ProfessorUpdateDTO(
                "Name2", "Surname2", "department2", null
        );


        String request = objectMapper.writeValueAsString(updateDTO);

        doAnswer(invocation -> {
            Long id = invocation.getArgument(0);
            ProfessorUpdateDTO updDTO = invocation.getArgument(1);
            return new ProfessorDTO(
                    id, updDTO.getName(), updDTO.getSurname(), updDTO.getDepartment(), null
            );
        }).when(professorService).updateProfessor(any(Long.class), any(ProfessorUpdateDTO.class));

        mockMvc.perform(put("/uni/professors/" + professorDTO.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(request))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(professorDTO.getId()))
                .andExpect(jsonPath("$.name").value(updateDTO.getName()))
                .andExpect(jsonPath("$.surname").value(updateDTO.getSurname()))
                .andExpect(jsonPath("$.department").value(updateDTO.getDepartment()));

        verify(professorService).updateProfessor(eq(1L), any(ProfessorUpdateDTO.class));
    }

    @Test
    void patchProfessor() throws Exception {
        ProfessorDTO professorDTO = new ProfessorDTO(
                1L, "Name", "Surname", "Department", null
        );

        ProfessorPatchDTO patchDTO = new ProfessorPatchDTO(
                null, null, "Department2", null
        );

        doAnswer(invocation -> {
            Long id = invocation.getArgument(0);
            ProfessorPatchDTO patch = invocation.getArgument(1);

            return new ProfessorDTO(
                    id,
                    patch.getName() != null ? patch.getName() : professorDTO.getName(),
                    patch.getSurname() != null ? patch.getSurname() : professorDTO.getSurname(),
                    patch.getDepartment() != null ? patch.getDepartment() : professorDTO.getDepartment(),
                    patch.getCourses() != null ? patch.getCourses() : professorDTO.getCourses()
            );
        }).when(professorService).patchProfessor(any(Long.class), any(ProfessorPatchDTO.class));

        String request = objectMapper.writeValueAsString(patchDTO);

        mockMvc.perform(patch("/uni/professors/" + professorDTO.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(request))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(professorDTO.getId()))
                .andExpect(jsonPath("$.name").value(professorDTO.getName()))
                .andExpect(jsonPath("$.surname").value(professorDTO.getSurname()))
                .andExpect(jsonPath("$.department").value(patchDTO.getDepartment()));

        verify(professorService).patchProfessor(eq(professorDTO.getId()), any(ProfessorPatchDTO.class));

    }

    @Test
    void deleteProfessor() throws Exception {
        ProfessorDTO dto = new ProfessorDTO(
                1L, "name", "surname", "department", null
        );

        doNothing().when(professorService).deleteProfessor(1L);

        mockMvc.perform(delete("/uni/professors/" + dto.getId()))
                .andExpect(status().isNoContent());

        verify(professorService).deleteProfessor(eq(1L));
    }
}
