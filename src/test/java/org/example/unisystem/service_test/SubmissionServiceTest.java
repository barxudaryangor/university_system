package org.example.unisystem.service_test;

import org.example.unisystem.dto.submission.SubmissionCreateDTO;
import org.example.unisystem.dto.submission.SubmissionDTO;
import org.example.unisystem.dto.submission.SubmissionPatchDTO;
import org.example.unisystem.dto.submission.SubmissionUpdateDTO;
import org.example.unisystem.entity.Submission;
import org.example.unisystem.jpa_repo.SubmissionJpaRepository;
import org.example.unisystem.mappers.SubmissionMapper;
import org.example.unisystem.patch.SubmissionPatchApplier;
import org.example.unisystem.service.SubmissionServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SubmissionServiceTest {
    @Mock
    SubmissionJpaRepository submissionJpaRepository;

    @Mock
    SubmissionMapper submissionMapper;

    @Mock
    SubmissionPatchApplier patchApplier;

    @InjectMocks
    SubmissionServiceImpl submissionService;

    @Test
    void getSubmissionById() {
        Submission submission = new Submission(
                1L, LocalDate.of(2026,11,11), new BigDecimal(10),
                null, null
       );

        when(submissionJpaRepository.findByIdGraph(1L)).thenReturn(Optional.of(submission));

        when(submissionMapper.submissionToDTO(submission)).thenReturn(
                new SubmissionDTO(
                        submission.getId(), submission.getSubmittedAt(), submission.getGrade(),
                        null, null
                )
        );

        SubmissionDTO submissionDTO = submissionService.getSubmissionById(1L);

        assertEquals(submission.getId(), submissionDTO.getId());
        assertEquals(submission.getSubmittedAt(), submissionDTO.getSubmittedAt());
        assertEquals(submission.getGrade(), submissionDTO.getGrade());

        verify(submissionJpaRepository).findByIdGraph(eq(1L));
    }

    @Test
    void getAllSubmissions() {
        Submission submission = new Submission(
                1L, LocalDate.of(2026,11,11), new BigDecimal(10),
                null, null
        );

        Submission submission2 = new Submission(
                2L, LocalDate.of(2027,11,11), new BigDecimal(20),
                null, null
        );

        when(submissionJpaRepository.findAll()).thenReturn(List.of(submission, submission2));

        when(submissionMapper.submissionToDTO(submission))
                .thenReturn(
                        new SubmissionDTO(
                                submission.getId(), submission.getSubmittedAt(), submission.getGrade(),
                                null, null
                        )
                );

        when(submissionMapper.submissionToDTO(submission2))
                .thenReturn(
                        new SubmissionDTO(
                                submission2.getId(), submission2.getSubmittedAt(), submission2.getGrade(),
                                null, null
                        )
                );

        List<SubmissionDTO> submissions = submissionService.getAllSubmissions();

        assertEquals(submission.getId(), submissions.get(0).getId());
        assertEquals(submission.getSubmittedAt(), submissions.get(0).getSubmittedAt());
        assertEquals(submission.getGrade(), submissions.get(0).getGrade());
        assertEquals(submission2.getId(), submissions.get(1).getId());
        assertEquals(submission2.getSubmittedAt(), submissions.get(1).getSubmittedAt());
        assertEquals(submission2.getGrade(), submissions.get(1).getGrade());

        verify(submissionJpaRepository).findAll();
    }

    @Test
    void createSubmission() {
        SubmissionCreateDTO submissionCreate = new SubmissionCreateDTO(
                LocalDate.of(2026,11,11), new BigDecimal(10)
                ,null ,null
        );

        Submission submission = new Submission(
                null, LocalDate.of(2026,11,11), new BigDecimal(10)
                ,null ,null
        );

        Submission savedSubmission = new Submission(
                1L, LocalDate.of(2026,11,11), new BigDecimal(10)
                ,null ,null
        );

        SubmissionDTO response = new SubmissionDTO(
                1L, LocalDate.of(2026,11,11), new BigDecimal(10)
                ,null ,null
        );

        when(submissionMapper.dtoToSubmission(submissionCreate)).thenReturn(submission);
        when(submissionJpaRepository.save(submission)).thenReturn(savedSubmission);
        when(submissionMapper.submissionToDTO(savedSubmission)).thenReturn(response);

        SubmissionDTO dto = submissionService.createSubmission(submissionCreate);

        assertEquals(1L, dto.getId());
        assertEquals(submissionCreate.getGrade(), dto.getGrade());
        assertEquals(submissionCreate.getSubmittedAt(), dto.getSubmittedAt());

        verify(submissionJpaRepository).save(submission);
    }

    @Test
    void updateSubmission() {
        Submission submission = new Submission(
                1L, LocalDate.of(1010,10,10), new BigDecimal(10),
                null, null
        );

        SubmissionUpdateDTO updateDTO = new SubmissionUpdateDTO(
                LocalDate.of(2020,12,12), new BigDecimal(20),
                null, null
        );

        Submission updatedSubmission = new Submission(
                1L, LocalDate.of(2020,12,12), new BigDecimal(20),
                null, null
        );

        SubmissionDTO dto = new SubmissionDTO(
                1L, LocalDate.of(2020,12,12), new BigDecimal(20),
                null, null
        );

        doAnswer(invocation -> {
            SubmissionUpdateDTO updateDTO2 = invocation.getArgument(0);
            Submission sub = invocation.getArgument(1);
            sub.setSubmittedAt(updateDTO2.getSubmittedAt());
            sub.setGrade(updateDTO2.getGrade());
            return null;
        }).when(submissionMapper).updateSubmissionFromDTO(any(SubmissionUpdateDTO.class), any(Submission.class));

        when(submissionJpaRepository.findByIdGraph(1L)).thenReturn(Optional.of(submission));
        when(submissionJpaRepository.save(submission)).thenReturn(updatedSubmission);
        when(submissionMapper.submissionToDTO(updatedSubmission)).thenReturn(dto);

        SubmissionDTO dto2 = submissionService.updateSubmission(1L, updateDTO);

        assertEquals(1L, dto2.getId());
        assertEquals(updateDTO.getSubmittedAt(), dto2.getSubmittedAt());
        assertEquals(updateDTO.getGrade(), dto2.getGrade());

        verify(submissionJpaRepository).save(submission);
    }

    @Test
    void patchSubmission() {
        Submission submission = new Submission(
                1L, LocalDate.of(2020,10,10), new BigDecimal(10),
                null, null
        );

        SubmissionPatchDTO patchDTO = new SubmissionPatchDTO(
                null, new BigDecimal(20), null, null
        );

        Submission patchedSubmission = new Submission(
                1L, LocalDate.of(2020,10,10), new BigDecimal(20),
                null, null
        );

        SubmissionDTO responseDTO = new SubmissionDTO(
                1L, LocalDate.of(2020,10,10), new BigDecimal(20),
                null, null
        );

        when(submissionJpaRepository.findByIdGraph(1L)).thenReturn(Optional.of(submission));

        doAnswer(invocation -> {
            Submission sub = invocation.getArgument(0);
            SubmissionPatchDTO patch = invocation.getArgument(1);
            if(patch.getGrade() != null) sub.setGrade(patch.getGrade());
            if(patch.getSubmittedAt() != null) sub.setSubmittedAt(patch.getSubmittedAt());
            return null;
        }).when(patchApplier).patchSubmission(any(Submission.class), any(SubmissionPatchDTO.class));

        when(submissionJpaRepository.save(submission)).thenReturn(patchedSubmission);
        when(submissionMapper.submissionToDTO(patchedSubmission)).thenReturn(responseDTO);

        SubmissionDTO returnedDTO = submissionService.patchSubmission(1L, patchDTO);

        assertEquals(1L, returnedDTO.getId());
        assertEquals(submission.getSubmittedAt(), returnedDTO.getSubmittedAt());
        assertEquals(patchDTO.getGrade(), returnedDTO.getGrade());

        verify(patchApplier).patchSubmission(eq(submission), eq(patchDTO));
    }

    @Test
    void deleteSubmission() {
        Submission submission = new Submission(
                1L, LocalDate.of(1010,10,10), new BigDecimal(10),
                null, null
        );

        when(submissionJpaRepository.findByIdGraph(1L)).thenReturn(Optional.of(submission));
        submissionService.deleteSubmission(1L);

        verify(submissionJpaRepository).findByIdGraph(1L);
        verify(submissionJpaRepository).delete(submission);
        verifyNoMoreInteractions(submissionJpaRepository);
    }
}
