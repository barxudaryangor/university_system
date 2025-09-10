package org.example.unisystem.patch;

import lombok.RequiredArgsConstructor;
import org.example.unisystem.dto.submission.SubmissionPatchDTO;
import org.example.unisystem.entity.Assignment;
import org.example.unisystem.entity.Student;
import org.example.unisystem.entity.Submission;
import org.example.unisystem.jpa_repo.AssignmentJpaRepository;
import org.example.unisystem.jpa_repo.StudentJpaRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SubmissionPatchApplier {

    private final StudentJpaRepository studentJpaRepository;
    private final AssignmentJpaRepository assignmentJpaRepository;

    public void patchSubmission(Submission submission, SubmissionPatchDTO submissionPatchDTO) {
        if(submissionPatchDTO.getGrade() != null) submission.setGrade(submissionPatchDTO.getGrade());
        if(submissionPatchDTO.getSubmittedAt() != null) submission.setSubmittedAt(submissionPatchDTO.getSubmittedAt());

        if(submissionPatchDTO.getStudent() != null) {
            if(submissionPatchDTO.getStudent().id() == null) {
                throw new IllegalArgumentException("student.id.is.required");
            }
            Student ref = studentJpaRepository.getReferenceById(submissionPatchDTO.getStudent().id());
            submission.setStudent(ref);
        }

        if(submissionPatchDTO.getAssignment() != null) {
            if(submissionPatchDTO.getAssignment().id() == null) {
                throw new IllegalArgumentException("assignment.id.is.required");
            }
            Assignment ref = assignmentJpaRepository.getReferenceById(submissionPatchDTO.getAssignment().id());
            submission.setAssignment(ref);
        }
    }
}
