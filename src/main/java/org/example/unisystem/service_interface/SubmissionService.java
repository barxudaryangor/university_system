package org.example.unisystem.service_interface;

import org.example.unisystem.dto.submission.SubmissionCreateDTO;
import org.example.unisystem.dto.submission.SubmissionDTO;
import org.example.unisystem.dto.submission.SubmissionPatchDTO;
import org.example.unisystem.dto.submission.SubmissionUpdateDTO;

import java.util.List;

public interface SubmissionService {
    SubmissionDTO getSubmissionById(Long id);
    List<SubmissionDTO> getAllSubmissions();
    SubmissionDTO createSubmission(SubmissionCreateDTO createDTO);
    SubmissionDTO updateSubmission(Long id, SubmissionUpdateDTO updateDTO);
    SubmissionDTO patchSubmission(Long id, SubmissionPatchDTO patchDTO);
    void deleteSubmission(Long id);
}
