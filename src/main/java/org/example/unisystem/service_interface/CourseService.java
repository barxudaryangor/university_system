package org.example.unisystem.service_interface;

import org.example.unisystem.dto.course.CourseCreateDTO;
import org.example.unisystem.dto.course.CourseDTO;
import org.example.unisystem.dto.course.CoursePatchDTO;
import org.example.unisystem.dto.course.CourseUpdateDTO;

import java.util.List;

public interface CourseService {
    CourseDTO getCourseById(Long id);
    List<CourseDTO> getAllCourses();
    CourseDTO createCourse(CourseCreateDTO createDTO);
    CourseDTO updateCourse(Long id, CourseUpdateDTO updateDTO);
    CourseDTO patchCourse(Long id, CoursePatchDTO patchDTO);
    void deleteCourse(Long id);

}
