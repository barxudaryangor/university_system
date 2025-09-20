package org.example.unisystem.converter;

import org.springframework.core.convert.converter.Converter;

import org.example.unisystem.short_dto.StudentShortDTO;
import org.springframework.stereotype.Component;


@Component
public class StringToStudentShortDTOConverter implements Converter<String, StudentShortDTO> {
    @Override
    public StudentShortDTO convert(String source) {
        if (source == null || source.trim().isEmpty()) {
            return null;
        }
        return new StudentShortDTO(Long.parseLong(source), null, null, null, null, null, null);
    }
}
