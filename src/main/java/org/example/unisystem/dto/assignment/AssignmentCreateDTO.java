package org.example.unisystem.dto.assignment;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.unisystem.short_dto.CourseShortDTO;
import org.example.unisystem.short_dto.SubmissionShortDTO;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AssignmentCreateDTO {

    @NotBlank(message = "title.is.required")
    @Size(min = 3, max = 50, message = "title.size.must.be.between.3.to.50")
    private String title;

    @NotNull(message = "due.date.is.required")
    @FutureOrPresent(message = "due.date.must.be.future.or.present")
    private LocalDate dueDate;

    private CourseShortDTO course;
    private List<SubmissionShortDTO> submissions;
}
