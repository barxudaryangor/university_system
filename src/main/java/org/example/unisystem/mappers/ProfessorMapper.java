package org.example.unisystem.mappers;

import org.example.unisystem.dto.professor.ProfessorCreateDTO;
import org.example.unisystem.dto.professor.ProfessorDTO;
import org.example.unisystem.dto.professor.ProfessorUpdateDTO;
import org.example.unisystem.entity.Course;
import org.example.unisystem.entity.Professor;
import org.example.unisystem.mapper_helper.ProfessorMapperHelper;
import org.mapstruct.*;

import java.util.Set;

@Mapper(componentModel = "spring", uses = ProfessorMapperHelper.class)
public interface ProfessorMapper {
    @Mapping(source = "courses", target = "courses", qualifiedByName = "coursesToShorts")
    ProfessorDTO professorToDTO(Professor professor);

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "courses", target = "courses", qualifiedByName = "shortsToCourses")
    Professor dtoToProfessor(ProfessorCreateDTO professorDTO);


    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
    @Mapping(target = "id", ignore = true)
    @Mapping(source = "courses", target = "courses", qualifiedByName = "shortsToCourses")
    void updateProfessorFromDTO(ProfessorUpdateDTO updateDTO, @MappingTarget Professor professor);

    @AfterMapping
    default void backLinksCourses(@MappingTarget Professor professor) {
        Set<Course> courses = professor.getCourses();
        if(courses != null) {
            for (Course c : courses) {
                if(c != null) c.setProfessor(professor);
            }
        }
    }
}
