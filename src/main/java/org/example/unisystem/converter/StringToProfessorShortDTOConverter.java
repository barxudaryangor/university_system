package org.example.unisystem.converter;

import org.example.unisystem.short_dto.ProfessorShortDTO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToProfessorShortDTOConverter implements Converter<String, ProfessorShortDTO> {
    @Override
    public ProfessorShortDTO convert(String source) {
        if (source == null || source.trim().isEmpty()) {
            return null;
        }
        return new ProfessorShortDTO(Long.parseLong(source), null, null, null);
    }
}

