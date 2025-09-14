package org.example.unisystem.mappers;

import org.example.unisystem.dto.submission.SubmissionCreateDTO;
import org.example.unisystem.dto.submission.SubmissionDTO;
import org.example.unisystem.dto.submission.SubmissionUpdateDTO;
import org.example.unisystem.entity.Submission;
import org.example.unisystem.mapper_helper.SubmissionMapperHelper;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = SubmissionMapperHelper.class)
public interface SubmissionMapper {

    @Mapping(target = "assignment", source = "assignment", qualifiedByName = "assignmentToShort")
    @Mapping(target = "student", source = "student", qualifiedByName = "studentToShort")
    SubmissionDTO submissionToDTO(Submission submission);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "assignment", source = "assignment", qualifiedByName = "shortToAssignment")
    @Mapping(target = "student", source = "student", qualifiedByName = "shortToStudent")
    Submission dtoToSubmission(SubmissionCreateDTO submissionDTO);

    @BeanMapping(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "assignment", source = "assignment", qualifiedByName = "shortToAssignment")
    @Mapping(target = "student", source = "student", qualifiedByName = "shortToStudent")
    void updateSubmissionFromDTO(SubmissionUpdateDTO updateDTO, @MappingTarget Submission submission);


}
