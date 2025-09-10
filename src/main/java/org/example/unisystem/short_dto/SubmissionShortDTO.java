package org.example.unisystem.short_dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record SubmissionShortDTO(
        Long id,
        LocalDate submittedAt,
        BigDecimal grade
) {
}
