package org.example.unisystem.patch;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.unisystem.dto.student.StudentPatchDTO;
import org.example.unisystem.entity.Course;
import org.example.unisystem.entity.Student;
import org.example.unisystem.entity.Submission;
import org.example.unisystem.jpa_repo.CourseJpaRepository;
import org.example.unisystem.jpa_repo.SubmissionJpaRepository;
import org.example.unisystem.short_dto.CourseShortDTO;
import org.example.unisystem.short_dto.SubmissionShortDTO;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class StudentPatchApplier {
    private final SubmissionJpaRepository submissionJpaRepository;
    private final CourseJpaRepository courseJpaRepository;

    @Transactional
    public void patchStudent(Student s, StudentPatchDTO dto) {
        if(dto.getName() != null) s.setName(dto.getName());
        if(dto.getSex() != null) s.setSex(dto.getSex());
        if(dto.getEmail() != null) s.setEmail(dto.getEmail());
        if(dto.getBirthdate() != null) s.setBirthdate(dto.getBirthdate());
        if(dto.getEnrolmentDate() != null) s.setEnrolmentDate(dto.getEnrolmentDate());
        if(dto.getSurname() != null) s.setSurname(dto.getSurname());


        if(dto.getCourses() != null) {
            Set<Long> incomingIds = dto.getCourses().stream()
                    .map(CourseShortDTO::id)
                    .collect(Collectors.toSet());

            s.getCourses().removeIf(c -> !incomingIds.contains(c.getId()));

            Set<Long> existingIds = s.getCourses().stream()
                    .map(Course::getId)
                    .collect(Collectors.toSet());

            for(Long id : incomingIds) {
                if(!existingIds.contains(id)) {
                    Course ref = courseJpaRepository.getReferenceById(id);
                    s.getCourses().add(ref);
                }
            }

        }

        if(dto.getSubmissions() != null) {
            Set<Long> incomingIds = dto.getSubmissions().stream()
                    .map(sd -> {
                        if(sd == null || sd.id() == null)
                            throw new IllegalArgumentException("submission.id.is.required");
                        return sd.id();
                    }).collect(Collectors.toSet());

            s.getSubmissions().removeIf(sub -> {
                boolean toRemove = sub.getId() != null && !incomingIds.contains(sub.getId());

                if(toRemove) {
                    sub.setStudent(null);
                }
                return toRemove;
            });

            Map<Long, Submission> currentById = s.getSubmissions().stream()
                    .filter(sub -> sub.getId() != null)
                    .collect(Collectors.toMap(Submission::getId, Function.identity()));

            for(SubmissionShortDTO sd : dto.getSubmissions()) {
                Submission sub = currentById.get(sd.id());
                if(sub == null) {
                    Submission ref = submissionJpaRepository.getReferenceById(sd.id());
                    ref.setStudent(s);
                    s.getSubmissions().add(ref);
                } else {
                    if (sd.submittedAt() != null) sub.setSubmittedAt(sd.submittedAt());
                    if (sd.grade() != null)       sub.setGrade(sd.grade());
                }
            }

        }
    }
}
