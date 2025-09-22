package org.example.unisystem.controller_test;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.example.unisystem.controller.AssignmentController;
import org.example.unisystem.dto.assignment.AssignmentCreateDTO;
import org.example.unisystem.dto.assignment.AssignmentDTO;
import org.example.unisystem.dto.assignment.AssignmentPatchDTO;
import org.example.unisystem.dto.assignment.AssignmentUpdateDTO;
import org.example.unisystem.pagination.PaginationResponse;
import org.example.unisystem.service_interface.AssignmentService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


import java.time.LocalDate;
import java.util.List;

@WebMvcTest(AssignmentController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AssignmentControllerTest {

    @MockitoBean
    AssignmentService assignmentService;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;

    @Test
    void getAssignmentById() throws Exception {
        AssignmentDTO assignment = new AssignmentDTO (
                1L, "title", LocalDate.of(2026,11,12),
                null, null
        );

        when(assignmentService.getAssignmentById(1L)).thenReturn(assignment);


        mockMvc.perform(get("/uni/assignments/" + assignment.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(assignment.getId()))
                .andExpect(jsonPath("$.title").value(assignment.getTitle()))
                .andExpect(jsonPath("$.dueDate").value(assignment.getDueDate().toString()));

        verify(assignmentService).getAssignmentById(any(Long.class));
    }

    @Test
    void getAllAssignments() throws Exception {
        AssignmentDTO assignment1 = new AssignmentDTO(
                1L, "Title", LocalDate.of(2026,11,11),
                null, null
        );

        AssignmentDTO assignment2 = new AssignmentDTO(
                2L, "Title2", LocalDate.of(2027,12,12),
                null, null
        );

        PaginationResponse<AssignmentDTO> response = new PaginationResponse<>(
                new PageImpl<>(
                        List.of(assignment1, assignment2), PageRequest.of(0,10), 2
                )
        );

        when(assignmentService.getAllAssignments(any(Pageable.class))).thenReturn(response);

        mockMvc.perform(get("/uni/assignments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(assignment1.getId()))
                .andExpect(jsonPath("$.content[0].dueDate").value(assignment1.getDueDate().toString()))
                .andExpect(jsonPath("$.content[0].title").value(assignment1.getTitle()))
                .andExpect(jsonPath("$.content[1].id").value(assignment2.getId()))
                .andExpect(jsonPath("$.content[1].dueDate").value(assignment2.getDueDate().toString()))
                .andExpect(jsonPath("$.content[1].title").value(assignment2.getTitle()))
                .andExpect(jsonPath("$.totalPages").value(response.getTotalPages()))
                .andExpect(jsonPath("$.totalElements").value(response.getTotalElements()))
                .andExpect(jsonPath("$.last").value(response.isLast()))
                .andExpect(jsonPath("$.pageNum").value(response.getPageNum()))
                .andExpect(jsonPath("$.pageNum").value(0));
    }

    @Test
    void createAssignment() throws Exception {
        AssignmentCreateDTO createDTO = new AssignmentCreateDTO(
                "Title", LocalDate.of(2026,11,11),
                null, null
        );

        AssignmentDTO responseDTO = new AssignmentDTO(
                1L, "Title", LocalDate.of(2026,11,11),
                null, null
        );

        String request = objectMapper.writeValueAsString(createDTO);

        when(assignmentService.createAssignment(any(AssignmentCreateDTO.class)))
                .thenReturn(responseDTO);

        mockMvc.perform(post("/uni/assignments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(responseDTO.getId()))
                .andExpect(jsonPath("$.title").value(responseDTO.getTitle()))
                .andExpect(jsonPath("$.dueDate").value(responseDTO.getDueDate().toString()));

        verify(assignmentService).createAssignment(any(AssignmentCreateDTO.class));
    }

    @Test
    void updateAssignment() throws Exception {
        AssignmentDTO assignment = new AssignmentDTO(
                1L, "Title", LocalDate.of(2026,11,11),
                null, null
        );

        AssignmentUpdateDTO updatedAssignment = new AssignmentUpdateDTO(
               "Title2", LocalDate.of(2027,12,12),
                null, null
        );

        doAnswer(invocation -> {
            Long id = invocation.getArgument(0);
            AssignmentUpdateDTO dto = invocation.getArgument(1);
            return new AssignmentDTO(
                    id, dto.getTitle(), dto.getDueDate(), null, null
            );
        }).when(assignmentService).updateAssignment(any(Long.class), any(AssignmentUpdateDTO.class));

        String request = objectMapper.writeValueAsString(updatedAssignment);

        mockMvc.perform(put("/uni/assignments/" + assignment.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(request))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dueDate").value(updatedAssignment.getDueDate().toString()))
                .andExpect(jsonPath("$.title").value(updatedAssignment.getTitle()));

        verify(assignmentService).updateAssignment(eq(assignment.getId()), any(AssignmentUpdateDTO.class));
    }

    @Test
    void patchAssignment() throws Exception {
        AssignmentDTO assignmentDTO = new AssignmentDTO(
                1L, "Title", LocalDate.of(2026,11,11),
                null,null
        );

        AssignmentPatchDTO patchDTO = new AssignmentPatchDTO(
                "Title2", LocalDate.of(2027,12,12),
                null,null
        );

        doAnswer(invocation -> {
            Long id = invocation.getArgument(0);
            AssignmentPatchDTO dto = invocation.getArgument(1);
            return new AssignmentDTO(
                    id, dto.getTitle(), dto.getDueDate(), null, null
            );
        }).when(assignmentService).patchAssignment(any(Long.class), any(AssignmentPatchDTO.class));

        String request = objectMapper.writeValueAsString(patchDTO);
        mockMvc.perform(patch("/uni/assignments/" + assignmentDTO.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(request))
                .andExpect(jsonPath("$.title").value(patchDTO.getTitle()))
                .andExpect(jsonPath("$.dueDate").value(patchDTO.getDueDate().toString()));

        verify(assignmentService).patchAssignment(eq(assignmentDTO.getId()), any(AssignmentPatchDTO.class));
    }

    @Test
    void deleteAssignment() throws Exception {
        AssignmentDTO dto = new AssignmentDTO(
                1L, "Title", LocalDate.of(2026,11,11),
                null, null
        );

        doNothing().when(assignmentService).deleteAssignment(1L);

        mockMvc.perform(delete("/uni/assignments/" + dto.getId()))
                .andExpect(status().isNoContent());
        verify(assignmentService).deleteAssignment(dto.getId());

    }
}
