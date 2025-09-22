package org.example.unisystem.end_to_end_test.professor;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.unisystem.dto.professor.ProfessorCreateDTO;
import org.example.unisystem.dto.professor.ProfessorDTO;
import org.example.unisystem.dto.professor.ProfessorPatchDTO;
import org.example.unisystem.dto.professor.ProfessorUpdateDTO;
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
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@Import(ContainerConfig.class)
public class ProfessorTest {

    @Autowired
    WebApplicationContext context;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    ProfessorJpaRepository professorJpaRepository;

    @Autowired
    StudentJpaRepository studentJpaRepository;

    @Autowired
    CourseJpaRepository courseJpaRepository;

    private MockMvc mockMvc;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setMockMvc() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        studentJpaRepository.deleteAll();
        courseJpaRepository.deleteAll();
        professorJpaRepository.deleteAll();
        jdbcTemplate.execute("ALTER SEQUENCE professor_id_seq RESTART WITH 1");
    }

    @Test
    void getProfessorById() throws Exception {
        ProfessorCreateDTO createDTO = new ProfessorCreateDTO(
                "name", "surname", "department", null
        );

        String request = objectMapper.writeValueAsString(createDTO);

        String response = mockMvc.perform(post("/uni/professors")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request)).andReturn().getResponse().getContentAsString();

        ProfessorDTO returnedDTO = objectMapper.readValue(response, ProfessorDTO.class);

        mockMvc.perform(get("/uni/professors/" + returnedDTO.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(returnedDTO.getId()))
                .andExpect(jsonPath("$.name").value(returnedDTO.getName()))
                .andExpect(jsonPath("$.surname").value(returnedDTO.getSurname()))
                .andExpect(jsonPath("$.department").value(returnedDTO.getDepartment()));
    }

    @Test
    void getAllProfessors() throws Exception {
        ProfessorCreateDTO createDTO = new ProfessorCreateDTO(
                "name", "surname", "department", null
        );

        String request = objectMapper.writeValueAsString(createDTO);

        String response = mockMvc.perform(post("/uni/professors")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request)).andReturn().getResponse().getContentAsString();

        ProfessorDTO returnedDTO = objectMapper.readValue(response, ProfessorDTO.class);

        ProfessorCreateDTO createDTO2 = new ProfessorCreateDTO(
                "name2", "surname2", "department2", null
        );

        String request2 = objectMapper.writeValueAsString(createDTO2);

        String response2 = mockMvc.perform(post("/uni/professors")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request2)).andReturn().getResponse().getContentAsString();

        ProfessorDTO returnedDTO2 = objectMapper.readValue(response2, ProfessorDTO.class);

        mockMvc.perform(get("/uni/professors?page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(returnedDTO.getId()))
                .andExpect(jsonPath("$.content[0].name").value(returnedDTO.getName()))
                .andExpect(jsonPath("$.content[0].surname").value(returnedDTO.getSurname()))
                .andExpect(jsonPath("$.content[0].department").value(returnedDTO.getDepartment()))
                .andExpect(jsonPath("$.content[1].id").value(returnedDTO2.getId()))
                .andExpect(jsonPath("$.content[1].name").value(returnedDTO2.getName()))
                .andExpect(jsonPath("$.content[1].surname").value(returnedDTO2.getSurname()))
                .andExpect(jsonPath("$.content[1].department").value(returnedDTO2.getDepartment()))
                .andExpect(jsonPath("$.pageSize").value(10))
                .andExpect(jsonPath("$.totalElements").value(2))
                .andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.last").value(true))
                .andExpect(jsonPath("$.pageNum").value(0));


    }

    @Test
    void createProfessor() throws Exception {
        ProfessorCreateDTO createDTO = new ProfessorCreateDTO(
                "name", "surname", "department", null
        );

        String request = objectMapper.writeValueAsString(createDTO);

        String response = mockMvc.perform(post("/uni/professors")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request)).andReturn().getResponse().getContentAsString();

        ProfessorDTO returnedDTO = objectMapper.readValue(response, ProfessorDTO.class);

        assertEquals(1L, returnedDTO.getId());
        assertEquals(createDTO.getName(), returnedDTO.getName());
        assertEquals(createDTO.getSurname(), returnedDTO.getSurname());
        assertEquals(createDTO.getDepartment(), returnedDTO.getDepartment());
    }

    @Test
    void updateProfessor() throws Exception {
        ProfessorDTO professorDTO = new ProfessorDTO(
                1L, "name", "surname", "department", null
        );

        ProfessorUpdateDTO updateDTO = new ProfessorUpdateDTO(
                "name2", "surname2", "department2", null
        );

        String request = objectMapper.writeValueAsString(professorDTO);

        String updateRequest = objectMapper.writeValueAsString(updateDTO);

        mockMvc.perform(post("/uni/professors")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request));

        mockMvc.perform(put("/uni/professors/" + 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(professorDTO.getId()))
                .andExpect(jsonPath("$.name").value(updateDTO.getName()))
                .andExpect(jsonPath("$.surname").value(updateDTO.getSurname()))
                .andExpect(jsonPath("$.department").value(updateDTO.getDepartment()));
    }

    @Test
    void patchProfessor() throws Exception {
        ProfessorCreateDTO professorDTO = new ProfessorCreateDTO(
               "name", "surname", "department", null
        );

        String request = objectMapper.writeValueAsString(professorDTO);

        ProfessorPatchDTO patchDTO = new ProfessorPatchDTO(
                null, "surname2", null, null
        );

        mockMvc.perform(post("/uni/professors")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request));

        String patchRequest = objectMapper.writeValueAsString(patchDTO);

        mockMvc.perform(patch("/uni/professors/" + 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(patchRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value(professorDTO.getName()))
                .andExpect(jsonPath("$.surname").value(patchDTO.getSurname()))
                .andExpect(jsonPath("$.department").value(professorDTO.getDepartment()));


    }

    @Test
    void deleteProfessor() throws Exception {
        ProfessorCreateDTO createDTO = new ProfessorCreateDTO(
                "name", "surname", "department", null
        );

        String request = objectMapper.writeValueAsString(createDTO);

        String response = mockMvc.perform(post("/uni/professors")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request)).andReturn().getResponse().getContentAsString();

        ProfessorDTO returnedDTO = objectMapper.readValue(response, ProfessorDTO.class);

        mockMvc.perform(delete("/uni/professors/" + returnedDTO.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/uni/professors/" + returnedDTO.getId()))
                .andExpect(status().isNotFound());
    }
}
