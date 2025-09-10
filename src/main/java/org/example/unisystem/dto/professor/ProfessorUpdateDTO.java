package org.example.unisystem.dto.professor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
public class ProfessorUpdateDTO {

    @NotBlank(message = "name.is.required")
    @Size(min = 3, max = 50, message = "name.size.must.be.between.3.to.50")
    private String name;

    @NotBlank(message = "surname.is.required")
    @Size(min = 3, max = 50, message = "surname.size.must.be.between.3.to.50")
    private String surname;

    @NotBlank(message = "department.is.required")
    @Size(min = 3, max = 50, message = "department.size.must.be.between.3.to.50")
    private String department;

    private List<CourseShortDTO> courses;
}
