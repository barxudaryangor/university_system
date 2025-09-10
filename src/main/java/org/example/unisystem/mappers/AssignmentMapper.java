package org.example.unisystem.mappers;

import org.example.unisystem.dto.assignment.AssignmentCreateDTO;
import org.example.unisystem.dto.assignment.AssignmentDTO;
import org.example.unisystem.dto.assignment.AssignmentUpdateDTO;
import org.example.unisystem.entity.Assignment;
import org.example.unisystem.entity.Submission;
import org.example.unisystem.mapper_helper.AssignmentMapperHelper;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = AssignmentMapperHelper.class)
public interface AssignmentMapper {
    @Mapping(target = "course", source = "course", qualifiedByName = "courseToShort")
    @Mapping(target = "submissions", source = "submissions", qualifiedByName = "submissionsToShorts")
    AssignmentDTO assignmentToDTO(Assignment assignment);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "course", source = "course", qualifiedByName = "shortToCourse")
    @Mapping(target = "submissions", source = "submissions", qualifiedByName = "shortsToSubmissions")
    Assignment dtoToAssignment(AssignmentCreateDTO assignmentDTO);


    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "course", source = "course", qualifiedByName = "shortToCourse")
    @Mapping(target = "submissions", source = "submissions", qualifiedByName = "shortsToSubmissions")
    void updateAssignmentFromDTO(AssignmentUpdateDTO assignmentDTO, @MappingTarget Assignment assignment);

    @AfterMapping
    default void linkSubmissions(@MappingTarget Assignment assignment) {
        if (assignment.getSubmissions() != null) {
            for (Submission s : assignment.getSubmissions()) {
                s.setAssignment(assignment);
            }
        }
    }
}
