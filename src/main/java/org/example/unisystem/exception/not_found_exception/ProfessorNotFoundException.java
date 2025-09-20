package org.example.unisystem.exception.not_found_exception;

public class ProfessorNotFoundException extends NotFoundException {
    public ProfessorNotFoundException(Long id) {
        super("professor.with.id." + id + ".not.found");
    }
}
