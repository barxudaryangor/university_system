package org.example.unisystem.controller_test;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.unisystem.controller.ProfessorController;
import org.example.unisystem.dto.professor.ProfessorCreateDTO;
import org.example.unisystem.dto.professor.ProfessorDTO;
import org.example.unisystem.dto.professor.ProfessorPatchDTO;
import org.example.unisystem.dto.professor.ProfessorUpdateDTO;
import org.example.unisystem.service_interface.ProfessorService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProfessorController.class)
public class ProfessorControllerTest {
    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    ProfessorService professorService;

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

        when(professorService.getAllProfessors()).thenReturn(List.of(professorDTO, professorDTO2));

        mockMvc.perform(get("/uni/professors"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(professorDTO.getId()))
                .andExpect(jsonPath("$[0].name").value(professorDTO.getName()))
                .andExpect(jsonPath("$[0].surname").value(professorDTO.getSurname()))
                .andExpect(jsonPath("$[0].department").value(professorDTO.getDepartment()))
                .andExpect(jsonPath("$[1].id").value(professorDTO2.getId()))
                .andExpect(jsonPath("$[1].name").value(professorDTO2.getName()))
                .andExpect(jsonPath("$[1].surname").value(professorDTO2.getSurname()))
                .andExpect(jsonPath("$[1].department").value(professorDTO2.getDepartment()));

        verify(professorService).getAllProfessors();
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
