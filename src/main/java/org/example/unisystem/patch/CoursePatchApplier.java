package org.example.unisystem.patch;

import lombok.RequiredArgsConstructor;
import org.example.unisystem.dto.course.CoursePatchDTO;
import org.example.unisystem.entity.Assignment;
import org.example.unisystem.entity.Course;
import org.example.unisystem.entity.Professor;
import org.example.unisystem.entity.Student;
import org.example.unisystem.jpa_repo.AssignmentJpaRepository;
import org.example.unisystem.jpa_repo.CourseJpaRepository;
import org.example.unisystem.jpa_repo.ProfessorJpaRepository;
import org.example.unisystem.jpa_repo.StudentJpaRepository;
import org.example.unisystem.short_dto.AssignmentShortDTO;
import org.example.unisystem.short_dto.StudentShortDTO;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class CoursePatchApplier {

    private final CourseJpaRepository courseJpaRepository;
    private final StudentJpaRepository studentJpaRepository;
    private final ProfessorJpaRepository professorJpaRepository;
    private final AssignmentJpaRepository assignmentJpaRepository;

    public void patchCourse(Course course, CoursePatchDTO patchDTO) {
        if(patchDTO.getTitle() != null) course.setTitle(patchDTO.getTitle());
        if(patchDTO.getCredits() != null) course.setCredits(patchDTO.getCredits());

        if(patchDTO.getStudents() != null) {
            Set<Long> incomingIds = patchDTO.getStudents()
                    .stream().map(StudentShortDTO::id)
                    .collect(Collectors.toSet());

            course.getStudents().removeIf(s -> !incomingIds.contains(s.getId()));

            Set<Long> existingIds = course.getStudents().stream()
                    .map(s-> s.getId())
                    .collect(Collectors.toSet());

            for(Long id : existingIds) {
                if(!incomingIds.contains(id)) {
                    Student ref = studentJpaRepository.getReferenceById(id);
                    course.getStudents().add(ref);
                }
            }
        }

        if(patchDTO.getProfessor() != null) {
            if(patchDTO.getProfessor().id() == null) {
                throw new IllegalArgumentException("professor.id.is.required");
            }
            Professor ref = professorJpaRepository.getReferenceById(patchDTO.getProfessor().id());
            course.setProfessor(ref);
        }

        if(patchDTO.getAssignments() != null) {
            Set<Long> incomingIds = patchDTO.getAssignments().stream()
                    .map( a -> {
                        if(a.id() == null || a == null) {
                            throw new IllegalArgumentException("assignment.id.is.required");
                        }
                        return a.id();
                    }).collect(Collectors.toSet());

            course.getAssignments().removeIf(a -> {
                boolean toRemove = a.getId() != null && !incomingIds.contains(a.getId());
                if(toRemove) {
                    a.setCourse(null);
                }
                return toRemove;
            });

            Map<Long, Assignment> currentById = course.getAssignments()
                    .stream().filter(a -> a.getId() != null)
                    .collect(Collectors.toMap(Assignment::getId, Function.identity()));

            for(AssignmentShortDTO aDTO : patchDTO.getAssignments()) {
                Assignment assignment = currentById.get(aDTO.id());
                if(assignment == null) {
                    Assignment ref = assignmentJpaRepository.getReferenceById(aDTO.id());
                    ref.setCourse(course);
                    course.getAssignments().add(ref);
                } else {
                    if(aDTO.dueDate() != null) assignment.setDueDate(aDTO.dueDate());
                    if(aDTO.title() != null) assignment.setTitle(aDTO.title());
                }
            }
        }
    }
}
