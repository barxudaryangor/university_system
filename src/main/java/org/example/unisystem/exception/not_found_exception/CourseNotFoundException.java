package org.example.unisystem.exception.not_found_exception;

public class CourseNotFoundException extends NotFoundException {
    public CourseNotFoundException(Long id) {
        super("course.with.id." + id + ".not.found");
    }
}
