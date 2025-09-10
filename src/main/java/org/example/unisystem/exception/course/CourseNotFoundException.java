package org.example.unisystem.exception.course;

public class CourseNotFoundException extends RuntimeException {
    public CourseNotFoundException(Long id) {
        super("course.with.id." + id + ".not.found");
    }
}
