package org.example.unisystem.dto.submission;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.unisystem.short_dto.AssignmentShortDTO;
import org.example.unisystem.short_dto.StudentShortDTO;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SubmissionUpdateDTO {
    @NotNull(message = "submitted.date.is.required")
    @Past(message = "submitted.date.must.be.in.past")
    private LocalDate submittedAt;

    @Positive(message = "grade.must.be.positive")
    private BigDecimal grade;

    private StudentShortDTO student;
    private AssignmentShortDTO assignment;
}
