package org.example.unisystem.service;

import lombok.RequiredArgsConstructor;
import org.example.unisystem.dto.submission.SubmissionCreateDTO;
import org.example.unisystem.dto.submission.SubmissionDTO;
import org.example.unisystem.dto.submission.SubmissionPatchDTO;
import org.example.unisystem.dto.submission.SubmissionUpdateDTO;
import org.example.unisystem.entity.Assignment;
import org.example.unisystem.entity.Student;
import org.example.unisystem.entity.Submission;
import org.example.unisystem.exception.not_found_exception.AssignmentNotFoundException;
import org.example.unisystem.exception.not_found_exception.StudentNotFoundException;
import org.example.unisystem.exception.not_found_exception.SubmissionNotFoundException;
import org.example.unisystem.jpa_repo.AssignmentJpaRepository;
import org.example.unisystem.jpa_repo.StudentJpaRepository;
import org.example.unisystem.jpa_repo.SubmissionJpaRepository;
import org.example.unisystem.mappers.SubmissionMapper;
import org.example.unisystem.pagination.PaginationResponse;
import org.example.unisystem.patch.SubmissionPatchApplier;
import org.example.unisystem.service_interface.SubmissionService;
import org.example.unisystem.update.SubmissionUpdateApplier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SubmissionServiceImpl implements SubmissionService {

    private final SubmissionJpaRepository submissionJpaRepository;
    private final SubmissionMapper submissionMapper;
    private final SubmissionPatchApplier patchApplier;
    private final StudentJpaRepository studentJpaRepository;
    private final AssignmentJpaRepository assignmentJpaRepository;
    private final SubmissionUpdateApplier updateApplier;

    @Override
    public SubmissionDTO getSubmissionById(Long id) {
        Submission submission = submissionJpaRepository.findByIdGraph(id)
                .orElseThrow(() -> new SubmissionNotFoundException(id));
        return submissionMapper.submissionToDTO(submission);
    }

    @Override
    public PaginationResponse<SubmissionDTO> getAllSubmissions(Pageable pageable) {
        Page<Submission> page = submissionJpaRepository.findAll(pageable);
        Page<SubmissionDTO> response = page.map(submissionMapper::submissionToDTO);
        return new PaginationResponse<>(response);
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
        updateApplier.updateSubmission(submission, updateDTO);
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

    @Override
    @Transactional
    public SubmissionDTO submitWork(Long studentId, Long assignmentId, SubmissionCreateDTO dto) {
        Student student = studentJpaRepository.findByIdGraph(studentId)
                .orElseThrow(() -> new StudentNotFoundException(studentId));

        Assignment assignment = assignmentJpaRepository.findByIdGraph(assignmentId)
                .orElseThrow(() -> new AssignmentNotFoundException(assignmentId));

        Submission submission = submissionMapper.dtoToSubmission(dto);
        submission.setStudent(student);
        submission.setAssignment(assignment);

        return submissionMapper.submissionToDTO(submissionJpaRepository.save(submission));
    }
}
