package org.example.unisystem;

import org.example.unisystem.converter.StringToCourseShortDTO;
import org.example.unisystem.converter.StringToSubmissionShortDTO;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {


    private final StringToCourseShortDTO courseConverter;
    private final StringToSubmissionShortDTO submissionConverter;

    public WebConfig(StringToCourseShortDTO courseConverter, StringToSubmissionShortDTO submissionConverter) {
        this.courseConverter = courseConverter;
        this.submissionConverter = submissionConverter;
    }

    @Bean
    public HiddenHttpMethodFilter hiddenHttpMethodFilter() {
        return new HiddenHttpMethodFilter();
    }


    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(courseConverter);
        registry.addConverter(submissionConverter);
    }
}
