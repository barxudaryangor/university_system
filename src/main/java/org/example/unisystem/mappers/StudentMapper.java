package org.example.unisystem.mappers;

import org.example.unisystem.dto.student.StudentDTO;
import org.example.unisystem.dto.student.StudentCreateDTO;
import org.example.unisystem.dto.student.StudentUpdateDTO;
import org.example.unisystem.entity.Course;
import org.example.unisystem.entity.Student;
import org.example.unisystem.entity.Submission;
import org.example.unisystem.mapper_helper.StudentMapperHelper;
import org.mapstruct.*;

import java.util.HashSet;

@Mapper(componentModel = "spring", uses = StudentMapperHelper.class)
public interface StudentMapper {
    @Mapping(source = "courses",     target = "courses",     qualifiedByName = "coursesToShorts")
    @Mapping(source = "submissions", target = "submissions", qualifiedByName = "submissionsToShorts")
    StudentDTO studentToDTO(Student student);

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "courses",     target = "courses",     qualifiedByName = "shortsToCourses")
    @Mapping(source = "submissions", target = "submissions", qualifiedByName = "shortsToSubmissions")
    Student dtoToStudent(StudentCreateDTO studentDTO);

    @BeanMapping(
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
            nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
    )
    @Mapping(target = "id", ignore = true)
    @Mapping(source = "courses",     target = "courses",     qualifiedByName = "shortsToCourses")
    @Mapping(source = "submissions", target = "submissions", qualifiedByName = "shortsToSubmissions")
    void updateStudentFromDto(StudentUpdateDTO studentDTO, @MappingTarget Student student);

    @AfterMapping
    default void backLink(@MappingTarget Student student) {
        if (student.getCourses() != null) {
            for (Course c : student.getCourses()) {
                if (c == null) continue;
                if (c.getStudents() == null) c.setStudents(new HashSet<>());
                if (!c.getStudents().contains(student)) c.getStudents().add(student);
            }
        }
        if (student.getSubmissions() != null) {
            for (Submission s : student.getSubmissions()) {
                if (s == null) continue;
                if (s.getStudent() != student) s.setStudent(student);
            }
        }
    }
}
