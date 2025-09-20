package org.example.unisystem.exception.not_found_exception;

public class AssignmentNotFoundException extends NotFoundException {
    public AssignmentNotFoundException(Long id) {
        super("assignment.with.id." + id + ".not.found");
    }
}
