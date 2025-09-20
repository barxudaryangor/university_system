package org.example.unisystem.converter;

import org.example.unisystem.short_dto.SubmissionShortDTO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToSubmissionShortDTO implements Converter<String, SubmissionShortDTO> {
    @Override
    public SubmissionShortDTO convert(String source) {
        if(source == null || source.isBlank()) {
            return null;
        }

        return new SubmissionShortDTO(Long.parseLong(source), null, null);
    }
}
