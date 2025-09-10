package org.example.unisystem.mapper_helper;

import lombok.RequiredArgsConstructor;
import org.example.unisystem.entity.Assignment;
import org.example.unisystem.entity.Student;
import org.example.unisystem.jpa_repo.AssignmentJpaRepository;
import org.example.unisystem.jpa_repo.StudentJpaRepository;
import org.example.unisystem.short_dto.AssignmentShortDTO;
import org.example.unisystem.short_dto.StudentShortDTO;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SubmissionMapperHelper {
    private final AssignmentJpaRepository assignmentJpaRepository;
    private final StudentJpaRepository studentJpaRepository;
    @Named("shortToAssignment")
    public Assignment shortToAssignment(AssignmentShortDTO assignmentDTO) {
        if(assignmentDTO == null) return null;
        return assignmentJpaRepository.findById(assignmentDTO.id())
                .orElseThrow(() -> new RuntimeException("assignment.not.found"));
    }

    @Named("shortToStudent")
    public Student shortToStudent(StudentShortDTO studentDTO) {
        if(studentDTO == null) return null;
        return studentJpaRepository.findById(studentDTO.id())
                .orElseThrow(() -> new RuntimeException("student.not.found"));
    }

    @Named("assignmentToShort")
    public AssignmentShortDTO assignmentToShort(Assignment a) {
        if(a == null) return null;
        return new AssignmentShortDTO(
                a.getId(), a.getTitle(), a.getDueDate()
        );
    }

    @Named("studentToShort")
    public StudentShortDTO studentToShort(Student s) {
        if(s == null) return null;
        return new StudentShortDTO(
                s.getId(), s.getName(), s.getSurname(), s.getSex(), s.getBirthdate(), s.getEnrolmentDate(), s.getEmail()
        );
    }
}
