package org.example.unisystem.mapper_helper;

import lombok.RequiredArgsConstructor;
import org.example.unisystem.entity.Assignment;
import org.example.unisystem.entity.Professor;
import org.example.unisystem.entity.Student;
import org.example.unisystem.jpa_repo.AssignmentJpaRepository;
import org.example.unisystem.jpa_repo.ProfessorJpaRepository;
import org.example.unisystem.jpa_repo.StudentJpaRepository;
import org.example.unisystem.short_dto.AssignmentShortDTO;
import org.example.unisystem.short_dto.ProfessorShortDTO;
import org.example.unisystem.short_dto.StudentShortDTO;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CourseMapperHelper {

    private final StudentJpaRepository studentJpaRepository;
    private final AssignmentJpaRepository assignmentJpaRepository;
    private final ProfessorJpaRepository professorJpaRepository;

    @Named("professorToShort")
    public ProfessorShortDTO professorToShort(Professor p) {
        if(p == null) return null;
        return new ProfessorShortDTO(p.getId(), p.getName(), p.getSurname(), p.getDepartment());
    }

    @Named("studentsToShorts")
    public List<StudentShortDTO> studentsToShorts(Set<Student> students) {
        if(students == null) return Collections.emptyList();

        return students.stream().filter(Objects::nonNull)
                .map(s -> new StudentShortDTO(
                        s.getId(), s.getName(), s.getSurname(),
                        s.getSex(), s.getBirthdate(), s.getEnrolmentDate(),
                        s.getEmail()
                ))
                .toList();
    }

    @Named("assignmentsToShorts")
    public List<AssignmentShortDTO> assignmentsToShorts(Set<Assignment> assignments) {
        if(assignments == null) return Collections.emptyList();

        return assignments.stream().filter(Objects::nonNull)
                .map(a -> new AssignmentShortDTO(
                        a.getId(), a.getTitle(), a.getDueDate()
                ))
                .toList();
    }

    @Named("shortToProfessorRef")
    public Professor shortToProfessorRef(ProfessorShortDTO dto) {
        if(dto == null || dto.id() == null) return null;
        return professorJpaRepository.findById(dto.id())
                .orElseThrow(() -> new RuntimeException("professor.not.found"));
    }

    @Named("shortsToStudentsRefs")
    public Set<Student> shortsToStudentsRefs(List<StudentShortDTO> shorts) {
        if(shorts == null) return Collections.emptySet();

        List<Long> ids = shorts.stream()
                .peek(s -> {
                    if(s == null) throw new IllegalArgumentException("student.dto.is.null");
                    if(s.id() == null) throw new IllegalArgumentException("student.id.is.absent");
                }).map(StudentShortDTO::id)
                .distinct()
                .toList();

        List<Student> students = studentJpaRepository.findAllById(ids);

        Map<Long, Student> byId = students.stream()
                .collect(Collectors.toMap(Student::getId, Function.identity()));

        List<Long> missing = ids.stream()
                .filter(id -> !byId.containsKey(id))
                .distinct()
                .toList();
        if(!missing.isEmpty()) {
            throw new IllegalArgumentException("student.not.found: " + missing);
        }

        return ids.stream().map(byId::get).collect(Collectors.toSet());
    }

    @Named("shortsToAssignmentsSmart")
    public Set<Assignment> shortsToAssignmentsSmart(List<AssignmentShortDTO> shorts) {
        if (shorts == null) return Collections.emptySet();

        List<Long> ids = shorts.stream()
                .peek(a -> {
                    if(a == null) throw new IllegalArgumentException("assignment.dto.is.null");
                    if(a.id() == null) throw new IllegalArgumentException("assignment.id.is.absent");
                }).map(AssignmentShortDTO::id)
                .distinct()
                .toList();

        List<Assignment> assignments = assignmentJpaRepository.findAllById(ids);

        Map<Long, Assignment> byId = assignments.stream()
                .collect(Collectors.toMap(Assignment::getId, Function.identity()));

        List<Long> missing = ids.stream()
                .filter(id -> !byId.containsKey(id))
                .distinct()
                .toList();
        if(!missing.isEmpty()) {
            throw new IllegalArgumentException("assignment.not:found: " + missing);
        }

        return ids.stream().map(byId::get).collect(Collectors.toSet());
    }
}
