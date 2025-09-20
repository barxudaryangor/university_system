package org.example.unisystem.converter;

import org.springframework.core.convert.converter.Converter;
import org.example.unisystem.short_dto.CourseShortDTO;
import org.springframework.stereotype.Component;

@Component
public class StringToCourseShortDTO implements Converter<String, CourseShortDTO> {

    @Override
    public CourseShortDTO convert(String source) {
        if (source == null || source.isBlank()) {
            return null;
        }
        return new CourseShortDTO(Long.parseLong(source), null, null);
    }
}
