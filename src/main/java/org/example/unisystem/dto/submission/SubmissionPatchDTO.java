package org.example.unisystem.dto.submission;

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
public class SubmissionPatchDTO {

    private LocalDate submittedAt;
    private BigDecimal grade;
    private StudentShortDTO student;
    private AssignmentShortDTO assignment;
}
