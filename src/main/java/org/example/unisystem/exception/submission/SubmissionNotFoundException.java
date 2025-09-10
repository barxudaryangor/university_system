package org.example.unisystem.exception.submission;

public class SubmissionNotFoundException extends RuntimeException {
    public SubmissionNotFoundException(Long id) {
        super("submission.with.id." + id + ".not.found");
    }
}
