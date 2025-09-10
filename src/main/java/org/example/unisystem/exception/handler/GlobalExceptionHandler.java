package org.example.unisystem.exception.handler;

import org.example.unisystem.exception.assignment.AssignmentNotFoundException;
import org.example.unisystem.exception.course.CourseNotFoundException;
import org.example.unisystem.exception.professor.ProfessorNotFoundException;
import org.example.unisystem.exception.student.StudentNotFoundException;
import org.example.unisystem.exception.submission.SubmissionNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({StudentNotFoundException.class})
    public ResponseEntity<String> handleStudentNotFound(StudentNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler({AssignmentNotFoundException.class})
    public ResponseEntity<String> handleAssignmentNotFound(AssignmentNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler({CourseNotFoundException.class})
    public ResponseEntity<String> handleCourseNotFound(CourseNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler({ProfessorNotFoundException.class})
    public ResponseEntity<String> handleProfessorNotFound(ProfessorNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler({SubmissionNotFoundException.class})
    public ResponseEntity<String> handleSubmissionNotFound(SubmissionNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
}
