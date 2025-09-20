package org.example.unisystem.mappers;


import org.example.unisystem.dto.course.CourseCreateDTO;
import org.example.unisystem.dto.course.CourseDTO;
import org.example.unisystem.dto.course.CourseUpdateDTO;
import org.example.unisystem.entity.Assignment;
import org.example.unisystem.entity.Course;
import org.example.unisystem.entity.Student;
import org.example.unisystem.mapper_helper.CourseMapperHelper;
import org.mapstruct.*;

import java.util.Set;

@Mapper(componentModel = "spring", uses = CourseMapperHelper.class)
public interface CourseMapper {
    @Mapping(target = "professor", source = "professor", qualifiedByName = "professorToShort")
    @Mapping(target = "students", source = "students", qualifiedByName = "studentsToShorts")
    @Mapping(target = "assignments", source = "assignments", qualifiedByName = "assignmentsToShorts")
    CourseDTO courseToDTO(Course course);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "professor", source = "professor", qualifiedByName = "shortToProfessorRef")
    @Mapping(target = "students", source = "students", qualifiedByName = "shortsToStudentsRefs")
    @Mapping(target = "assignments", source = "assignments", qualifiedByName = "shortsToAssignmentsSmart")
    Course dtoToCourse(CourseCreateDTO courseDTO);

    @AfterMapping
    default void backLinkAssignments(@MappingTarget Course course) {
        Set<Assignment> list = course.getAssignments();

        if(list != null) {
            for(Assignment a : list) {
                if(a != null) a.setCourse(course);
            }
        }
    }

    @AfterMapping
    default void linkStudents(@MappingTarget Course course) {
        if (course.getStudents() != null) {
            for (Student s : course.getStudents()) {
                s.getCourses().add(course); // двунаправленная синхронизация
            }
        }
    }
}
