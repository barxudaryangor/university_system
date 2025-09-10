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
public class StudentMapperHelper {
    private final CourseJpaRepository courseJpaRepository;
    private final SubmissionJpaRepository submissionJpaRepository;

    @Named("coursesToShorts")
    public List<CourseShortDTO> coursesToShorts(Set<Course> courses) {
        if(courses == null) return Collections.emptyList();
        return courses.stream().filter(Objects::nonNull)
                .map(c -> new CourseShortDTO(
                        c.getId(), c.getTitle(), c.getCredits()
                )).toList();
    }

    @Named("submissionsToShorts")
    public List<SubmissionShortDTO> submissionsToShorts(Set<Submission> submissions) {
        if(submissions == null) return Collections.emptyList();
        return submissions.stream().filter(Objects::nonNull)
                .map(s -> new SubmissionShortDTO(
                        s.getId(), s.getSubmittedAt(), s.getGrade()
                ))
                .toList();
    }

    @Named("shortsToCourses")
    public Set<Course> shortsToCourses(List<CourseShortDTO> coursesDTOs) {
        if(coursesDTOs == null) return Collections.emptySet();

        List<Long> ids = coursesDTOs.stream()
                .peek(c -> {
                    if(c == null) throw new IllegalArgumentException("course.dto.is.null");
                    if(c.id() == null) throw new IllegalArgumentException("course.id.is.absent");
                }).map(CourseShortDTO::id)
                .distinct()
                .toList();

        List<Course> courses = courseJpaRepository.findAllById(ids);

        Map<Long, Course> byId = courses.stream()
                .collect(Collectors.toMap(Course::getId, Function.identity()));

        List<Long> missing = ids.stream()
                .filter(id -> !byId.containsKey(id))
                .distinct()
                .toList();

        if(!missing.isEmpty()) {
            throw new IllegalArgumentException("course.not.found" + missing);
        }

        return ids.stream().map(byId::get).collect(Collectors.toSet());
    }

    @Named("shortsToSubmissions")
    public Set<Submission> shortsToSubmissions(List<SubmissionShortDTO> submissionsDTOs) {
        if(submissionsDTOs == null) return Collections.emptySet();

        List<Long> ids = submissionsDTOs.stream()
                .peek(s -> {
                    if(s == null) throw new IllegalArgumentException("submission.dto.is.null");
                    if(s.id() == null) throw new IllegalArgumentException("submission.id.is.absent");
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
            throw new IllegalArgumentException("submissions.not.found: " + missing);
        }

        return ids.stream().map(byId::get).collect(Collectors.toSet());
    }
}
