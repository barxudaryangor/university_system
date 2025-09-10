package org.example.unisystem.dto.student;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.unisystem.enums.Gender;
import org.example.unisystem.short_dto.CourseShortDTO;
import org.example.unisystem.short_dto.SubmissionShortDTO;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudentPatchDTO {
    private String name;
    private String surname;
    private Gender sex;
    private LocalDate birthdate;
    private LocalDate enrolmentDate;

    @Email(message = "email.must.be.valid")
    private String email;

    private List<CourseShortDTO> courses;
    private List<SubmissionShortDTO> submissions;
}
