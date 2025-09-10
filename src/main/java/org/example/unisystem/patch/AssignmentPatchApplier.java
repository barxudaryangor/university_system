package org.example.unisystem.patch;

import lombok.RequiredArgsConstructor;
import org.example.unisystem.dto.assignment.AssignmentPatchDTO;
import org.example.unisystem.entity.Assignment;
import org.example.unisystem.entity.Course;
import org.example.unisystem.entity.Submission;
import org.example.unisystem.jpa_repo.AssignmentJpaRepository;
import org.example.unisystem.jpa_repo.CourseJpaRepository;
import org.example.unisystem.jpa_repo.SubmissionJpaRepository;
import org.example.unisystem.short_dto.SubmissionShortDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class AssignmentPatchApplier {

    private final CourseJpaRepository courseJpaRepository;
    private final SubmissionJpaRepository submissionJpaRepository;

    public void patchAssignment(Assignment assignment, AssignmentPatchDTO assignmentDTO) {
        if(assignmentDTO.getTitle() != null) assignment.setTitle(assignmentDTO.getTitle());
        if(assignmentDTO.getDueDate() != null) assignment.setDueDate(assignmentDTO.getDueDate());

        if(assignmentDTO.getCourse() != null) {
            if(assignmentDTO.getCourse().id() == null) {
                throw new IllegalArgumentException("course.id.is.required");
            }
            Course course = courseJpaRepository.getReferenceById(assignmentDTO.getCourse().id());
            assignment.setCourse(course);
        }

        if(assignmentDTO.getSubmissions() != null) {
            Set<Long> incomingIds = assignmentDTO.getSubmissions().stream()
                    .map(sDTO -> {
                        if(sDTO == null || sDTO.id() == null) {
                            throw new IllegalArgumentException("submission.id.is.required");
                        }
                        return sDTO.id();
                    }).collect(Collectors.toSet());

            assignment.getSubmissions().removeIf(sub -> {
                boolean toRemove = sub.getId() != null && !incomingIds.contains(sub.getId());
                if (toRemove) {
                    sub.setAssignment(null);
                }
                return toRemove;
            });

            Map<Long, Submission> currentById = assignment.getSubmissions()
                    .stream().filter(sub -> sub.getId() != null)
                    .collect(Collectors.toMap(Submission::getId, Function.identity()));

            for(SubmissionShortDTO sd : assignmentDTO.getSubmissions()) {
                Submission sub = currentById.get(sd.id());
                if(sub == null) {
                    Submission ref = submissionJpaRepository.getReferenceById(sd.id());
                    ref.setAssignment(assignment);
                    assignment.getSubmissions().add(ref);
                } else {
                   if(sd.submittedAt() != null) sub.setSubmittedAt(sd.submittedAt());
                   if(sd.grade() != null) sub.setGrade(sd.grade());
                }
            }
        }
    }
}
