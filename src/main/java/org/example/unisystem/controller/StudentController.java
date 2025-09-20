package org.example.unisystem.controller;

import jakarta.validation.Valid;
import org.example.unisystem.dto.student.StudentDTO;
import org.example.unisystem.dto.student.StudentCreateDTO;
import org.example.unisystem.dto.student.StudentPatchDTO;
import org.example.unisystem.dto.student.StudentUpdateDTO;
import org.example.unisystem.dto.submission.SubmissionCreateDTO;
import org.example.unisystem.dto.submission.SubmissionDTO;
import org.example.unisystem.pagination.PaginationResponse;
import org.example.unisystem.service_interface.StudentService;
import org.example.unisystem.service_interface.SubmissionService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/uni/students")
@Validated
public class StudentController {
    private final StudentService studentService;
    private final SubmissionService submissionService;

    public StudentController(StudentService studentService, SubmissionService submissionService) {
        this.studentService = studentService;
        this.submissionService = submissionService;
    }

    @GetMapping("/{id}")
    StudentDTO getStudentById(@PathVariable Long id) {
        return studentService.getStudentById(id);
    }

    @GetMapping
    public PaginationResponse<StudentDTO> getAllStudents(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page,size);
        return studentService.getAllStudents(pageable);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    StudentDTO createStudent(@Valid @RequestBody StudentCreateDTO studentDTO) {
        return studentService.createStudent(studentDTO);
    }

    @PostMapping("/{studentId}/courses/{courseId}")
    @ResponseStatus(HttpStatus.CREATED)
    StudentDTO addStudentToCourse(@PathVariable Long studentId, @PathVariable Long courseId) {
        return studentService.addStudentToCourse(studentId, courseId);
    }

    @PostMapping("/{studentId}/assignments/{assignmentId}/submissions")
    @ResponseStatus(HttpStatus.CREATED)
    SubmissionDTO submitWork(@PathVariable Long studentId, @PathVariable Long assignmentId,
                             @Validated @RequestBody SubmissionCreateDTO dto) {
        return submissionService.submitWork(studentId, assignmentId, dto);
    }

    @PutMapping("/{id}")
    StudentDTO updateStudent(@PathVariable Long id, @Validated @RequestBody StudentUpdateDTO studentDTO) {
        return studentService.updateStudent(id, studentDTO);
    }

    @PatchMapping("/{id}")
    StudentDTO patchStudent(@PathVariable Long id, @RequestBody StudentPatchDTO studentDTO) {
        return studentService.patchStudent(id, studentDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{studentId}/courses/{courseId}")
    ResponseEntity<Void> deleteStudentFromCourse(@PathVariable Long studentId, @PathVariable Long courseId) {
        studentService.deleteStudentFromCourse(studentId, courseId);
        return ResponseEntity.noContent().build();
    }

}
