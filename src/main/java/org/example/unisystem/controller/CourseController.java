package org.example.unisystem.controller;

import lombok.RequiredArgsConstructor;
import org.example.unisystem.dto.course.CourseCreateDTO;
import org.example.unisystem.dto.course.CourseDTO;
import org.example.unisystem.dto.course.CoursePatchDTO;
import org.example.unisystem.dto.course.CourseUpdateDTO;
import org.example.unisystem.service_interface.CourseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/uni/courses")
@Validated
public class CourseController {
    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping("/{id}")
    CourseDTO getCourseById(@PathVariable Long id) {
        return courseService.getCourseById(id);
    }

    @GetMapping
    List<CourseDTO> getAllCourses() {
        return courseService.getAllCourses();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    CourseDTO createCourse(@Validated @RequestBody CourseCreateDTO dto) {
        return courseService.createCourse(dto);
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
