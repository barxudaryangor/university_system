package org.example.unisystem.end_to_end_test.assignment;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.unisystem.dto.assignment.AssignmentCreateDTO;
import org.example.unisystem.dto.assignment.AssignmentDTO;
import org.example.unisystem.dto.assignment.AssignmentPatchDTO;
import org.example.unisystem.dto.assignment.AssignmentUpdateDTO;
import org.example.unisystem.end_to_end_test.container.ContainerConfig;
import org.example.unisystem.jpa_repo.AssignmentJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@Import(ContainerConfig.class)
public class AssignmentTest {

    @Autowired
    WebApplicationContext context;

    @Autowired
    ObjectMapper objectMapper;

    private MockMvc mockMvc;

    @Autowired
    AssignmentJpaRepository assignmentJpaRepository;

    @BeforeEach
    void setMockMvc() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        assignmentJpaRepository.deleteAll();
    }

    @Test
    void getAssignmentById() throws Exception {
        AssignmentCreateDTO request = new AssignmentCreateDTO(
                "Title", LocalDate.of(2026,11,11),
                null,null
        );

        String requestBody = objectMapper.writeValueAsString(request);

        String response = mockMvc.perform(post("/uni/assignments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)).andReturn()
                .getResponse().getContentAsString();

        AssignmentDTO created = objectMapper.readValue(response, AssignmentDTO.class);

        mockMvc.perform(get("/uni/assignments/" + created.getId()))
                .andExpect(jsonPath("$.id").value(created.getId()))
                .andExpect(jsonPath("$.dueDate").value(created.getDueDate().toString()))
                .andExpect(jsonPath("$.title").value(created.getTitle()));

    }

    @Test
    void getAllAssignments() throws Exception {
        AssignmentCreateDTO request = new AssignmentCreateDTO(
                "Title", LocalDate.of(2026,11,11),
                null,null
        );

        String requestBody = objectMapper.writeValueAsString(request);

        String response = mockMvc.perform(post("/uni/assignments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)).andReturn()
                .getResponse().getContentAsString();

        AssignmentDTO created = objectMapper.readValue(response, AssignmentDTO.class);

        AssignmentCreateDTO request2 = new AssignmentCreateDTO(
                "Title2", LocalDate.of(2027,11,11),
                null,null
        );

        String requestBody2 = objectMapper.writeValueAsString(request2);

        String response2 = mockMvc.perform(post("/uni/assignments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody2)).andReturn()
                .getResponse().getContentAsString();

        AssignmentDTO created2 = objectMapper.readValue(response2, AssignmentDTO.class);

        mockMvc.perform(get("/uni/assignments"))
                .andExpect(jsonPath("$[0].id").value(created.getId()))
                .andExpect(jsonPath("$[0].title").value(created.getTitle()))
                .andExpect(jsonPath("$[0].dueDate").value(created.getDueDate().toString()))
                .andExpect(jsonPath("$[1].id").value(created2.getId()))
                .andExpect(jsonPath("$[1].title").value(created2.getTitle()))
                .andExpect(jsonPath("$[1].dueDate").value(created2.getDueDate().toString()));
    }

    @Test
    void createAssignment() throws Exception {
        AssignmentCreateDTO request = new AssignmentCreateDTO(
                "Title", LocalDate.of(2026,11,11),
                null,null
        );

        String requestBody = objectMapper.writeValueAsString(request);

        String response = mockMvc.perform(post("/uni/assignments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andReturn().getResponse()
                .getContentAsString();

        AssignmentDTO dto = objectMapper.readValue(response, AssignmentDTO.class);

        assertEquals(request.getTitle(), dto.getTitle());
        assertEquals(request.getDueDate(), dto.getDueDate());

    }

    @Test
    void updateAssignment() throws Exception {
        AssignmentCreateDTO request = new AssignmentCreateDTO(
                "Title", LocalDate.of(2026,11,11),
                null,null
        );

        String requestBody = objectMapper.writeValueAsString(request);

        String response = mockMvc.perform(post("/uni/assignments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andReturn().getResponse()
                .getContentAsString();

        AssignmentDTO dto = objectMapper.readValue(response, AssignmentDTO.class);

        AssignmentUpdateDTO updateDTO = new AssignmentUpdateDTO(
                "Title2", LocalDate.of(2027,12,12),
                null, null
        );

        String updateRequest = objectMapper.writeValueAsString(updateDTO);

        mockMvc.perform(put("/uni/assignments/" + dto.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(dto.getId()))
                .andExpect(jsonPath("$.title").value(updateDTO.getTitle()))
                .andExpect(jsonPath("$.dueDate").value(updateDTO.getDueDate().toString()));

    }

    @Test
    void patchAssignment() throws Exception {
        AssignmentCreateDTO request = new AssignmentCreateDTO(
                "Title", LocalDate.of(2026,11,11),
                null,null
        );

        String requestBody = objectMapper.writeValueAsString(request);

        String response = mockMvc.perform(post("/uni/assignments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)).andReturn()
                .getResponse().getContentAsString();

        AssignmentDTO created = objectMapper.readValue(response, AssignmentDTO.class);

        AssignmentPatchDTO patch = new AssignmentPatchDTO(
                "Title2", LocalDate.of(2027,12,12),
                null, null
        );

        String patchBody = objectMapper.writeValueAsString(patch);

        mockMvc.perform(patch("/uni/assignments/" + created.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(patchBody))
                .andExpect(jsonPath("$.dueDate").value(patch.getDueDate().toString()))
                .andExpect(jsonPath("$.title").value(patch.getTitle()));
    }

    @Test
    void deleteAssignment() throws Exception {
        AssignmentCreateDTO request = new AssignmentCreateDTO(
                "Title", LocalDate.of(2026,11,11),
                null,null
        );

        String requestBody = objectMapper.writeValueAsString(request);

        String response = mockMvc.perform(post("/uni/assignments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)).andReturn()
                .getResponse().getContentAsString();

        AssignmentDTO created = objectMapper.readValue(response, AssignmentDTO.class);

        mockMvc.perform(delete("/uni/assignments/" + created.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/uni/assignments/" +created.getId()))
                .andExpect(status().isNotFound());
    }
}
