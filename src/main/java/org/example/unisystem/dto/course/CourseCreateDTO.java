package org.example.unisystem.dto.course;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
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
@AllArgsConstructor
@NoArgsConstructor
public class CourseCreateDTO {

    @NotBlank(message = "title.is.required")
    @Size(min = 3, max = 50, message = "title.size.must.be.between.3.to.50")
    private String title;

    @NotBlank(message = "credits.is.required")
    @Positive(message = "credits.must.be.positive")
    private Integer credits;

    private List<StudentShortDTO> students;
    private ProfessorShortDTO professor;
    private List<AssignmentShortDTO> assignments;

}
