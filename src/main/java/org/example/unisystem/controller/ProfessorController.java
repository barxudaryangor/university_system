package org.example.unisystem.controller;

import org.example.unisystem.dto.assignment.AssignmentCreateDTO;
import org.example.unisystem.dto.assignment.AssignmentDTO;
import org.example.unisystem.dto.course.CourseCreateDTO;
import org.example.unisystem.dto.course.CourseDTO;
import org.example.unisystem.dto.professor.ProfessorCreateDTO;
import org.example.unisystem.dto.professor.ProfessorDTO;
import org.example.unisystem.dto.professor.ProfessorPatchDTO;
import org.example.unisystem.dto.professor.ProfessorUpdateDTO;
import org.example.unisystem.pagination.PaginationResponse;
import org.example.unisystem.service_interface.AssignmentService;
import org.example.unisystem.service_interface.CourseService;
import org.example.unisystem.service_interface.ProfessorService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/uni/professors")
public class ProfessorController {
    private final ProfessorService professorService;
    private final CourseService courseService;
    private final AssignmentService assignmentService;

    public ProfessorController(ProfessorService professorService, CourseService courseService, AssignmentService assignmentService) {
        this.professorService = professorService;
        this.courseService = courseService;
        this.assignmentService = assignmentService;
    }

    @GetMapping("/{id}")
    ProfessorDTO getProfessorById(@PathVariable Long id) {
        return professorService.getProfessorById(id);
    }

    @GetMapping
    PaginationResponse<ProfessorDTO> getAllProfessors(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page,size);
        return professorService.getAllProfessors(pageable);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    ProfessorDTO createProfessor(@Validated @RequestBody ProfessorCreateDTO createDTO) {
        return professorService.createProfessor(createDTO);
    }

    @PostMapping("/{professorId}/courses")
    @ResponseStatus(HttpStatus.CREATED)
    CourseDTO createCourseByProfessor(@PathVariable Long professorId, @Validated @RequestBody CourseCreateDTO dto) {
        return courseService.createCourseByProfessor(professorId, dto);
    }

    @PostMapping("/{professorId}/courses/{courseId}/assignments")
    @ResponseStatus(HttpStatus.CREATED)
    AssignmentDTO createAssignmentForCourse(@PathVariable Long professorId, @PathVariable Long courseId,
                                            @Validated @RequestBody AssignmentCreateDTO dto) {
        return assignmentService.createAssignmentForCourse(professorId, courseId, dto);
    }

    @PutMapping("/{id}")
    ProfessorDTO updateProfessor(@PathVariable Long id, @Validated @RequestBody ProfessorUpdateDTO updateDTO) {
        return professorService.updateProfessor(id, updateDTO);
    }

    @PatchMapping("/{id}")
    ProfessorDTO patchProfessor(@PathVariable Long id, @Validated @RequestBody ProfessorPatchDTO patchDTO) {
        return professorService.patchProfessor(id, patchDTO);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteProfessor(@PathVariable Long id) {
        professorService.deleteProfessor(id);
        return ResponseEntity.noContent().build();
    }
}
