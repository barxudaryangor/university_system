package org.example.unisystem.mapper_helper;


import lombok.RequiredArgsConstructor;
import org.example.unisystem.entity.Course;
import org.example.unisystem.jpa_repo.CourseJpaRepository;
import org.example.unisystem.short_dto.CourseShortDTO;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ProfessorMapperHelper {
    private final CourseJpaRepository courseJpaRepository;

    @Named("coursesToShorts")
    public List<CourseShortDTO> coursesToShorts(Set<Course> courses) {
        if(courses == null) return Collections.emptyList();
        return courses.stream().filter(Objects::nonNull)
                .map(c -> new CourseShortDTO(
                        c.getId(), c.getTitle(), c.getCredits()
                )).toList();
    }

    @Named("shortsToCourses")
    public Set<Course> shortsTOCourses(List<CourseShortDTO> shorts) {
        if(shorts == null) return Collections.emptySet();

        List<Long> ids = shorts.stream()
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
            throw new IllegalArgumentException("course.not.found: " + missing);
        }

        return ids.stream().map(byId::get).collect(Collectors.toSet());

    }
}

