package org.example.unisystem.converter;

import org.example.unisystem.short_dto.AssignmentShortDTO;
import org.springframework.stereotype.Component;
import org.springframework.core.convert.converter.Converter;


@Component
public class StringToAssignmentShortDTOConverter implements Converter<String, AssignmentShortDTO> {
    @Override
    public AssignmentShortDTO convert(String source) {
        if (source == null || source.trim().isEmpty()) {
            return null;
        }
        return new AssignmentShortDTO(Long.parseLong(source), null, null);
    }
}
