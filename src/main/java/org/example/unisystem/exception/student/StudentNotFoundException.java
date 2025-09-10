package org.example.unisystem.exception.student;

public class StudentNotFoundException extends RuntimeException {
    public StudentNotFoundException(Long id) {
        super("student.with.id." + id + ".not.found");
    }
}
