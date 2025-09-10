package org.example.unisystem.short_dto;

import java.time.LocalDate;

public record AssignmentShortDTO(
        Long id,
        String title,
        LocalDate dueDate
) {
}
