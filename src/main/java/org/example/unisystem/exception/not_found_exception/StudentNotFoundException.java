package org.example.unisystem.exception.not_found_exception;

public class StudentNotFoundException extends NotFoundException {
    public StudentNotFoundException(Long id) {
        super("student.with.id." + id + ".not.found");
    }
}
