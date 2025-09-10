package org.example.unisystem.dto.professor;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.unisystem.short_dto.CourseShortDTO;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProfessorPatchDTO {

    private String name;
    private String surname;
    private String department;
    private List<CourseShortDTO> courses;
}
