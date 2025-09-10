package org.example.unisystem.dto.student;

import jakarta.validation.constraints.*;
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
public class StudentCreateDTO {
        @NotBlank(message = "name.can.not.be.blank")
        @Size(min = 2, max = 50, message = "name.must.be.between.2.and.50.characters")
        private String name;

        @NotBlank(message = "surname.can.not.be.blank")
        @Size(min = 2, max = 50, message = "surname.must.be.between.2.and.50.characters")
        private String surname;

        @NotNull(message = "gender.is.required")
        private Gender sex;

        @NotNull(message = "birthdate.is.required")
        @Past(message = "birthdate.must.be.in.the.past")
        private LocalDate birthdate;

        @NotNull(message = "enrolment.date.is.required")
        @PastOrPresent(message = "enrolment.date.can.not.be.in.the.future")
        private LocalDate enrolmentDate;

        @NotBlank(message = "email.must.be.required")
        @Email(message = "email.must.be.valid")
        private String email;

        private List<CourseShortDTO> courses;
        private List<SubmissionShortDTO> submissions;
    }

