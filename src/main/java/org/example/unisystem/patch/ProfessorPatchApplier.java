package org.example.unisystem.patch;

import lombok.RequiredArgsConstructor;
import org.example.unisystem.dto.professor.ProfessorPatchDTO;
import org.example.unisystem.entity.Course;
import org.example.unisystem.entity.Professor;
import org.example.unisystem.jpa_repo.CourseJpaRepository;
import org.example.unisystem.short_dto.CourseShortDTO;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ProfessorPatchApplier {
    private final CourseJpaRepository courseJpaRepository;

    public void patchProfessor(Professor professor, ProfessorPatchDTO professorDTO) {
        if(professorDTO.getName() != null) professor.setName(professorDTO.getName());
        if(professorDTO.getSurname() != null) professor.setSurname(professorDTO.getSurname());
        if(professorDTO.getDepartment() != null) professor.setDepartment(professorDTO.getDepartment());

        if(professorDTO.getCourses() != null) {

            Set<Long> incomingIds = professorDTO.getCourses()
                    .stream().map( cDTO -> {
                        if(cDTO.id() == null || cDTO == null) {
                            throw new IllegalArgumentException("course.id.is.required");
                        }
                        return cDTO.id();
                    }).collect(Collectors.toSet());

            professor.getCourses().removeIf(c -> {
                boolean toRemove = c.getId() != null && !incomingIds.contains(c.getId());
                if(toRemove) {
                    c.setProfessor(null);
                }
                return toRemove;
            });

            Map<Long, Course> currentById = professor.getCourses()
                    .stream().filter(c -> c.getId() != null)
                    .collect(Collectors.toMap(Course::getId, Function.identity()));

            for(CourseShortDTO cDTO : professorDTO.getCourses()) {
                Course course = currentById.get(cDTO.id());
                if(course == null) {
                    Course ref = courseJpaRepository.getReferenceById(cDTO.id());
                    ref.setProfessor(professor);
                    professor.getCourses().add(ref);
                } else {
                    if (cDTO.title() != null) course.setTitle(cDTO.title());
                    if (cDTO.credits() != null) course.setCredits(cDTO.credits());
                }
            }
        }
    }
}
