package org.example.unisystem.exception.assignment;

public class AssignmentNotFoundException extends RuntimeException {
    public AssignmentNotFoundException(Long id) {
        super("assignment.with.id." + id + ".not.found");
    }
}
