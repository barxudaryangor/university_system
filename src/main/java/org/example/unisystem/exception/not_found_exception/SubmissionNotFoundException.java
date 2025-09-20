package org.example.unisystem.exception.not_found_exception;

public class SubmissionNotFoundException extends NotFoundException {
    public SubmissionNotFoundException(Long id) {
        super("submission.with.id." + id + ".not.found");
    }
}
