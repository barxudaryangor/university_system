package org.example.unisystem.mapper_helper;

import lombok.RequiredArgsConstructor;
import org.example.unisystem.entity.Course;
import org.example.unisystem.entity.Submission;
import org.example.unisystem.jpa_repo.CourseJpaRepository;
import org.example.unisystem.jpa_repo.SubmissionJpaRepository;
import org.example.unisystem.short_dto.CourseShortDTO;
import org.example.unisystem.short_dto.SubmissionShortDTO;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class AssignmentMapperHelper {
    private final CourseJpaRepository courseJpaRepository;
    private final SubmissionJpaRepository submissionJpaRepository;

    @Named("courseToShort")
    public CourseShortDTO courseToShort(Course c) {
        if(c == null) return null;
        return new CourseShortDTO(
                c.getId(), c.getTitle(), c.getCredits()
        );
    }

    @Named("submissionsToShorts")
    public List<SubmissionShortDTO> submissionsToShorts(Set<Submission> submissions) {
        if(submissions == null) return Collections.emptyList();
        return submissions.stream().filter(Objects::nonNull)
                .map(s -> new SubmissionShortDTO(
                        s.getId(), s.getSubmittedAt(), s.getGrade()
                )).toList();
    }

    @Named("shortToCourse")
    public Course shortToCourse(CourseShortDTO dto) {
        if(dto == null) return null;
        return courseJpaRepository.findById(dto.id())
                .orElseThrow(() -> new RuntimeException("course.not.found"));
    }

    @Named("shortsToSubmissions")
    public Set<Submission> shortsToSubmissions(List<SubmissionShortDTO> shorts) {
        if(shorts == null) return null;

        List<Long> ids = shorts.stream()
                .peek(s ->{
                    if(s == null) throw new IllegalArgumentException("submission.dto.is.null");
                    if(s.id() == null) throw new IllegalArgumentException("submission.id.is.null");
                }).map(SubmissionShortDTO::id)
                .distinct()
                .toList();

        List<Submission> submissions = submissionJpaRepository.findAllById(ids);

        Map<Long, Submission> byId = submissions.stream()
                .collect(Collectors.toMap(Submission::getId, Function.identity()));

        List<Long> missing = ids.stream()
                .filter(id -> !byId.containsKey(id))
                .distinct()
                .toList();
        if(!missing.isEmpty()) {
            throw new IllegalArgumentException("submission.not.found: " + missing);
        }

        return ids.stream().map(byId::get).collect(Collectors.toSet());

    }
}
