package org.example.unisystem.end_to_end_test.submission;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.unisystem.dto.submission.SubmissionCreateDTO;
import org.example.unisystem.dto.submission.SubmissionDTO;
import org.example.unisystem.dto.submission.SubmissionPatchDTO;
import org.example.unisystem.dto.submission.SubmissionUpdateDTO;
import org.example.unisystem.end_to_end_test.container.ContainerConfig;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@Import(ContainerConfig.class)
public class SubmissionTest {
    @Autowired
    WebApplicationContext context;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    SubmissionJpaRepository submissionJpaRepository;

    private MockMvc mockMvc;

    @BeforeEach
    void setMockMvc() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        submissionJpaRepository.deleteAll();
    }

    @Test
    void getSubmissionById() throws Exception {
        SubmissionCreateDTO createDTO = new SubmissionCreateDTO(
                LocalDate.of(20,10,10), new BigDecimal(10), null,null
        );

        String request = objectMapper.writeValueAsString(createDTO);

        String response = mockMvc.perform(post("/uni/submissions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request)).andReturn().getResponse().getContentAsString();

        SubmissionDTO dto = objectMapper.readValue(response, SubmissionDTO.class);

        mockMvc.perform(get("/uni/submissions/" + 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(dto.getId()))
                .andExpect(jsonPath("$.submittedAt").value(dto.getSubmittedAt().toString()))
                .andExpect(jsonPath("$.grade").value(dto.getGrade()));
    }

    @Test
    void getAllSubmissions() throws Exception {
        SubmissionCreateDTO createDTO = new SubmissionCreateDTO(
                LocalDate.of(20,10,10), new BigDecimal(10), null,null
        );

        String request = objectMapper.writeValueAsString(createDTO);

        String response = mockMvc.perform(post("/uni/submissions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request)).andReturn().getResponse().getContentAsString();

        SubmissionDTO dto = objectMapper.readValue(response, SubmissionDTO.class);

        SubmissionCreateDTO createDTO2 = new SubmissionCreateDTO(
                LocalDate.of(30,5,5), new BigDecimal(5), null,null
        );

        String request2 = objectMapper.writeValueAsString(createDTO2);

        String response2 = mockMvc.perform(post("/uni/submissions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request2)).andReturn().getResponse().getContentAsString();

        SubmissionDTO dto2 = objectMapper.readValue(response2, SubmissionDTO.class);

        mockMvc.perform(get("/uni/submissions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(dto.getId()))
                .andExpect(jsonPath("$[0].grade").value(dto.getGrade()))
                .andExpect(jsonPath("$[0].submittedAt").value(dto.getSubmittedAt().toString()))
                .andExpect(jsonPath("$[1].id").value(dto2.getId()))
                .andExpect(jsonPath("$[1].grade").value(dto2.getGrade()))
                .andExpect(jsonPath("$[1].submittedAt").value(dto2.getSubmittedAt().toString()));
    }

    @Test
    void createSubmission() throws Exception {
        SubmissionCreateDTO createDTO = new SubmissionCreateDTO(
                LocalDate.of(20,10,10), new BigDecimal(10), null,null
        );

        String request = objectMapper.writeValueAsString(createDTO);

        mockMvc.perform(post("/uni/submissions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.submittedAt").value(createDTO.getSubmittedAt().toString()))
                .andExpect(jsonPath("$.grade").value(createDTO.getGrade()));
    }

    @Test
    void updateSubmission() throws Exception {
        SubmissionCreateDTO createDTO = new SubmissionCreateDTO(
                LocalDate.of(20,10,10), new BigDecimal(10), null,null
        );

        String request = objectMapper.writeValueAsString(createDTO);

        String response = mockMvc.perform(post("/uni/submissions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request)).andReturn().getResponse().getContentAsString();

        SubmissionDTO dto = objectMapper.readValue(response, SubmissionDTO.class);

        SubmissionUpdateDTO updateDTO = new SubmissionUpdateDTO(
                LocalDate.of(2020,11,11), new BigDecimal(20),
                null, null
        );

        String updateResponse = objectMapper.writeValueAsString(updateDTO);

        mockMvc.perform(put("/uni/submissions/" + 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateResponse))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(dto.getId()))
                .andExpect(jsonPath("$.grade").value(updateDTO.getGrade()))
                .andExpect(jsonPath("$.submittedAt").value(updateDTO.getSubmittedAt().toString()));
    }

    @Test
    void patchSubmission() throws Exception {
        SubmissionCreateDTO createDTO = new SubmissionCreateDTO(
                LocalDate.of(20,10,10), new BigDecimal(10), null,null
        );

        String request = objectMapper.writeValueAsString(createDTO);

        String response = mockMvc.perform(post("/uni/submissions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request)).andReturn().getResponse().getContentAsString();

        SubmissionDTO dto = objectMapper.readValue(response, SubmissionDTO.class);

        SubmissionPatchDTO patchDTO = new SubmissionPatchDTO(
                LocalDate.of(50,10, 20), new BigDecimal(20),null,null
        );

        String requestPatch = objectMapper.writeValueAsString(patchDTO);

        mockMvc.perform(patch("/uni/submissions/" + 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestPatch))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(dto.getId()))
                .andExpect(jsonPath("$.submittedAt").value(patchDTO.getSubmittedAt().toString()))
                .andExpect(jsonPath("$.grade").value(patchDTO.getGrade()));
    }

    @Test
    void deleteSubmission() throws Exception {
        SubmissionCreateDTO createDTO = new SubmissionCreateDTO(
                LocalDate.of(20,10,10), new BigDecimal(10), null,null
        );

        String request = objectMapper.writeValueAsString(createDTO);

        String response = mockMvc.perform(post("/uni/submissions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request)).andReturn().getResponse().getContentAsString();

        SubmissionDTO dto = objectMapper.readValue(response, SubmissionDTO.class);

        mockMvc.perform(delete("/uni/submissions/" + dto.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/uni/submissions/" + dto.getId()))
                .andExpect(status().isNotFound());
    }
}
