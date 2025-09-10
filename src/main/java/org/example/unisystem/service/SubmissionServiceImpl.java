package org.example.unisystem.service;

import lombok.RequiredArgsConstructor;
import org.example.unisystem.dto.submission.SubmissionCreateDTO;
import org.example.unisystem.dto.submission.SubmissionDTO;
import org.example.unisystem.dto.submission.SubmissionPatchDTO;
import org.example.unisystem.dto.submission.SubmissionUpdateDTO;
import org.example.unisystem.entity.Submission;
import org.example.unisystem.exception.submission.SubmissionNotFoundException;
import org.example.unisystem.jpa_repo.SubmissionJpaRepository;
import org.example.unisystem.mappers.SubmissionMapper;
import org.example.unisystem.patch.SubmissionPatchApplier;
import org.example.unisystem.service_interface.SubmissionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SubmissionServiceImpl implements SubmissionService {

    private final SubmissionJpaRepository submissionJpaRepository;
    private final SubmissionMapper submissionMapper;
    private final SubmissionPatchApplier patchApplier;

    @Override
    public SubmissionDTO getSubmissionById(Long id) {
        Submission submission = submissionJpaRepository.findByIdGraph(id)
                .orElseThrow(() -> new SubmissionNotFoundException(id));
        return submissionMapper.submissionToDTO(submission);
    }

    @Override
    public List<SubmissionDTO> getAllSubmissions() {
        return submissionJpaRepository.findAll().stream()
                .map(submissionMapper::submissionToDTO)
                .toList();
    }

    @Override
    @Transactional
    public SubmissionDTO createSubmission(SubmissionCreateDTO createDTO) {
        Submission submission = submissionMapper.dtoToSubmission(createDTO);
        return submissionMapper.submissionToDTO(submissionJpaRepository.save(submission));
    }

    @Override
    @Transactional
    public SubmissionDTO updateSubmission(Long id, SubmissionUpdateDTO updateDTO) {
        Submission submission = submissionJpaRepository.findByIdGraph(id)
                .orElseThrow(() -> new SubmissionNotFoundException(id));
        submissionMapper.updateSubmissionFromDTO(updateDTO, submission);
        return submissionMapper.submissionToDTO(submissionJpaRepository.save(submission));
    }

    @Override
    @Transactional
    public SubmissionDTO patchSubmission(Long id, SubmissionPatchDTO patchDTO) {
        Submission submission = submissionJpaRepository.findByIdGraph(id)
                .orElseThrow(() -> new SubmissionNotFoundException(id));
        patchApplier.patchSubmission(submission, patchDTO);
        return submissionMapper.submissionToDTO(submissionJpaRepository.save(submission));
    }

    @Override
    @Transactional
    public void deleteSubmission(Long id) {
        Submission submission = submissionJpaRepository.findByIdGraph(id)
                .orElseThrow(() -> new SubmissionNotFoundException(id));
        submissionJpaRepository.delete(submission);
    }
}
