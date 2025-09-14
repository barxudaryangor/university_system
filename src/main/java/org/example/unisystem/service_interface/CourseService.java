package org.example.unisystem.service_interface;

import org.example.unisystem.dto.course.CourseCreateDTO;
import org.example.unisystem.dto.course.CourseDTO;
import org.example.unisystem.dto.course.CoursePatchDTO;
import org.example.unisystem.dto.course.CourseUpdateDTO;
import org.example.unisystem.pagination.PaginationResponse;
import org.springframework.data.domain.Pageable;

public interface CourseService {
    CourseDTO getCourseById(Long id);
    PaginationResponse<CourseDTO> getAllCourses(Pageable pageable);
    CourseDTO createCourse(CourseCreateDTO createDTO);
    CourseDTO updateCourse(Long id, CourseUpdateDTO updateDTO);
    CourseDTO patchCourse(Long id, CoursePatchDTO patchDTO);
    void deleteCourse(Long id);
    CourseDTO createCourseByProfessor(Long professorId, CourseCreateDTO dto);

}
