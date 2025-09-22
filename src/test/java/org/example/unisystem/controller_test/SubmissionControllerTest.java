package org.example.unisystem.controller_test;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.unisystem.controller.SubmissionController;
import org.example.unisystem.dto.submission.SubmissionCreateDTO;
import org.example.unisystem.dto.submission.SubmissionDTO;
import org.example.unisystem.dto.submission.SubmissionPatchDTO;
import org.example.unisystem.dto.submission.SubmissionUpdateDTO;
import org.example.unisystem.pagination.PaginationResponse;
import org.example.unisystem.service_interface.SubmissionService;
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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SubmissionController.class)
@AutoConfigureMockMvc(addFilters = false)
public class SubmissionControllerTest {
    @MockitoBean
    SubmissionService submissionService;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;


    @Test
    void getSubmissionById() throws Exception {
        SubmissionDTO dto = new SubmissionDTO(
                1L, LocalDate.of(2020,10,10), new BigDecimal(10), null, null
        );

        when(submissionService.getSubmissionById(1L)).thenReturn(dto);

        mockMvc.perform(get("/uni/submissions/" + dto.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(dto.getId()))
                .andExpect(jsonPath("$.submittedAt").value(dto.getSubmittedAt().toString()))
                .andExpect(jsonPath("$.grade").value(dto.getGrade()));

        verify(submissionService).getSubmissionById(eq(1L));
    }

    @Test
    void getAllSubmissions() throws Exception {
        SubmissionDTO dto = new SubmissionDTO(
                1L, LocalDate.of(2020,10,10), new BigDecimal(10), null, null
        );

        SubmissionDTO dto2 = new SubmissionDTO(
                2L, LocalDate.of(1010,10,10), new BigDecimal(20), null, null
        );

        PaginationResponse<SubmissionDTO> response =
                new PaginationResponse<>(
                    new PageImpl<>(List.of(dto,dto2),
                            PageRequest.of(0,10), 2)
        );


        when(submissionService.getAllSubmissions(any(Pageable.class))).thenReturn(response);

        mockMvc.perform(get("/uni/submissions?page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(dto.getId()))
                .andExpect(jsonPath("$.content[0].grade").value(dto.getGrade()))
                .andExpect(jsonPath("$.content[0].submittedAt").value(dto.getSubmittedAt().toString()))
                .andExpect(jsonPath("$.content[1].id").value(dto2.getId()))
                .andExpect(jsonPath("$.content[1].grade").value(dto2.getGrade()))
                .andExpect(jsonPath("$.content[1].submittedAt").value(dto2.getSubmittedAt().toString()))
                .andExpect(jsonPath("$.totalPages").value(response.getTotalPages()))
                .andExpect(jsonPath("$.totalElements").value(response.getTotalElements()))
                .andExpect(jsonPath("$.last").value(response.isLast()))
                .andExpect(jsonPath("$.pageNum").value(response.getPageNum()))
                .andExpect(jsonPath("$.pageNum").value(0));

        verify(submissionService).getAllSubmissions(any(Pageable.class));

    }

    @Test
    void createSubmission() throws Exception {
        SubmissionCreateDTO createDTO = new SubmissionCreateDTO(
                LocalDate.of(2020,10,10), new BigDecimal(10), null, null
        );

        SubmissionDTO submissionDTO = new SubmissionDTO(
                1L, LocalDate.of(2020,10,10), new BigDecimal(10), null, null
        );

        String request = objectMapper.writeValueAsString(createDTO);

        when(submissionService.createSubmission(any(SubmissionCreateDTO.class)))
                .thenReturn(submissionDTO);

        mockMvc.perform(post("/uni/submissions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(submissionDTO.getId()))
                .andExpect(jsonPath("$.grade").value(submissionDTO.getGrade()))
                .andExpect(jsonPath("$.submittedAt").value(submissionDTO.getSubmittedAt().toString()));

    }

    @Test
    void updateSubmission() throws Exception {
        SubmissionDTO submissionDTO = new SubmissionDTO(
                1L, LocalDate.of(1010,10,10), new BigDecimal(10), null, null
        );

        SubmissionUpdateDTO updateDTO = new SubmissionUpdateDTO(
                LocalDate.of(2020, 10, 20), new BigDecimal(20), null, null
        );

        doAnswer(invocation -> {
            Long id = invocation.getArgument(0);
            SubmissionUpdateDTO u = invocation.getArgument(1);
            return new SubmissionDTO(
                    id, u.getSubmittedAt(), u.getGrade(), null, null
            );
        }).when(submissionService).updateSubmission(any(Long.class), any(SubmissionUpdateDTO.class));

        String request = objectMapper.writeValueAsString(updateDTO);

        mockMvc.perform(put("/uni/submissions/" + submissionDTO.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(request))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.grade").value(updateDTO.getGrade()))
                .andExpect(jsonPath("$.submittedAt").value(updateDTO.getSubmittedAt().toString()));

        verify(submissionService).updateSubmission(eq(1L), any(SubmissionUpdateDTO.class));
    }

    @Test
    void patchSubmission() throws Exception {
        SubmissionDTO submissionDTO = new SubmissionDTO(
                1L, LocalDate.of(1010,10,10), new BigDecimal(10),
                null, null
        );

        SubmissionPatchDTO patchDTO = new SubmissionPatchDTO(
                LocalDate.of(2020,12,12), new BigDecimal(20),
                null, null
        );

        doAnswer(invocation -> {
            Long id = invocation.getArgument(0);
            SubmissionPatchDTO p = invocation.getArgument(1);
            return new SubmissionDTO(
                    id, p.getSubmittedAt(), p.getGrade(), null, null
            );
        }).when(submissionService).patchSubmission(any(Long.class), any(SubmissionPatchDTO.class));

        String request = objectMapper.writeValueAsString(patchDTO);

        mockMvc.perform(patch("/uni/submissions/" + submissionDTO.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(request))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.submittedAt").value(patchDTO.getSubmittedAt().toString()))
                .andExpect(jsonPath("$.grade").value(patchDTO.getGrade()));
        verify(submissionService).patchSubmission(eq(1L), any(SubmissionPatchDTO.class));
    }

    @Test
    void deleteSubmission() throws Exception {
        SubmissionDTO submissionDTO = new SubmissionDTO(
                1L, LocalDate.of(1010,10,10), new BigDecimal(10), null, null
        );

        doNothing().when(submissionService).deleteSubmission(1L);

        mockMvc.perform(delete("/uni/submissions/" + submissionDTO.getId()))
                .andExpect(status().isNoContent());
        verify(submissionService).deleteSubmission(eq(1L));
    }
}
