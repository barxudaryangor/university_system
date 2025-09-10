package org.example.unisystem.dto.course;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.unisystem.short_dto.AssignmentShortDTO;
import org.example.unisystem.short_dto.ProfessorShortDTO;
import org.example.unisystem.short_dto.StudentShortDTO;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CourseDTO {

    private Long id;
    private String title;
    private Integer credits;
    private List<StudentShortDTO> students;
    private ProfessorShortDTO professor;
    private List<AssignmentShortDTO> assignments;

}
