package org.example.unisystem.controller;

import org.example.unisystem.dto.assignment.AssignmentCreateDTO;
import org.example.unisystem.dto.assignment.AssignmentDTO;
import org.example.unisystem.dto.course.CourseCreateDTO;
import org.example.unisystem.dto.course.CourseDTO;
import org.example.unisystem.dto.course.CoursePatchDTO;
import org.example.unisystem.dto.course.CourseUpdateDTO;
import org.example.unisystem.pagination.PaginationResponse;
import org.example.unisystem.service_interface.AssignmentService;
import org.example.unisystem.service_interface.CourseService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/uni/courses")
@Validated
public class CourseController {
    private final CourseService courseService;
    private final AssignmentService assignmentService;

    public CourseController(CourseService courseService, AssignmentService assignmentService) {
        this.courseService = courseService;
        this.assignmentService = assignmentService;
    }

    @GetMapping("/{id}")
    CourseDTO getCourseById(@PathVariable Long id) {
        return courseService.getCourseById(id);
    }

    @GetMapping
    PaginationResponse<CourseDTO> getAllCourses(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page,size);
        return courseService.getAllCourses(pageable);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    CourseDTO createCourse(@Validated @RequestBody CourseCreateDTO dto) {
        return courseService.createCourse(dto);
    }

    @PostMapping("/{courseId}/assignments")
    @ResponseStatus(HttpStatus.CREATED)
    AssignmentDTO createAssignmentForCourse(@PathVariable Long courseId, @Validated @RequestBody AssignmentCreateDTO dto) {
        return assignmentService.createAssignmentForCourse(null, courseId, dto);
    }

    @PutMapping("/{id}")
    CourseDTO updateCourse(@PathVariable Long id, @Validated @RequestBody CourseUpdateDTO dto) {
        return courseService.updateCourse(id, dto);
    }

    @PatchMapping("/{id}")
    CourseDTO patchCourse(@PathVariable Long id, @Validated @RequestBody CoursePatchDTO dto) {
        return courseService.patchCourse(id, dto);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
        return ResponseEntity.noContent().build();
    }
}
