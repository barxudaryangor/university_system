package org.example.unisystem.exception.professor;

public class ProfessorNotFoundException extends RuntimeException {
    public ProfessorNotFoundException(Long id) {
        super("professor.with.id." + id + ".not.found");
    }
}
