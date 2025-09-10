package org.example.unisystem.short_dto;

import org.example.unisystem.enums.Gender;

import java.time.LocalDate;

public record StudentShortDTO(
        Long id,
        String name,
        String surname,
        Gender sex,
        LocalDate birthdate,
        LocalDate enrolmentDate,
        String email
) {
}
