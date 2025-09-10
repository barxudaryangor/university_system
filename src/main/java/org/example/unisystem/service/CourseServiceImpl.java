package org.example.unisystem.service;

import lombok.RequiredArgsConstructor;
import org.example.unisystem.dto.course.CourseCreateDTO;
import org.example.unisystem.dto.course.CourseDTO;
import org.example.unisystem.dto.course.CoursePatchDTO;
import org.example.unisystem.dto.course.CourseUpdateDTO;
import org.example.unisystem.entity.Course;
import org.example.unisystem.exception.course.CourseNotFoundException;
import org.example.unisystem.jpa_repo.CourseJpaRepository;
import org.example.unisystem.mappers.CourseMapper;
import org.example.unisystem.patch.CoursePatchApplier;
import org.example.unisystem.service_interface.CourseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CourseServiceImpl implements CourseService {

    private final CourseJpaRepository courseJpaRepository;
    private final CourseMapper courseMapper;
    private final CoursePatchApplier patchApplier;

    @Override
    public CourseDTO getCourseById(Long id) {
        Course course = courseJpaRepository.findByIdGraph(id)
                .orElseThrow(() -> new CourseNotFoundException(id));
        return courseMapper.courseToDTO(course);
    }

    @Override
    public List<CourseDTO> getAllCourses() {
        return courseJpaRepository.findAll().stream()
                .map(courseMapper::courseToDTO)
                .toList();
    }

    @Override
    @Transactional
    public CourseDTO createCourse(CourseCreateDTO createDTO) {
        Course course = courseMapper.dtoToCourse(createDTO);
        return courseMapper.courseToDTO(courseJpaRepository.save(course));
    }

    @Override
    @Transactional
    public CourseDTO updateCourse(Long id, CourseUpdateDTO updateDTO) {
        Course course = courseJpaRepository.findByIdGraph(id)
                .orElseThrow(() -> new CourseNotFoundException(id));
        courseMapper.updateCourseFromDTO(updateDTO, course);
        return courseMapper.courseToDTO(course);
    }

    @Override
    @Transactional
    public CourseDTO patchCourse(Long id, CoursePatchDTO patchDTO) {
        Course course = courseJpaRepository.findByIdGraph(id)
                .orElseThrow(() -> new CourseNotFoundException(id));
        patchApplier.patchCourse(course, patchDTO);
        return courseMapper.courseToDTO(courseJpaRepository.save(course));
    }

    @Override
    @Transactional
    public void deleteCourse(Long id) {
        Course course = courseJpaRepository.findByIdGraph(id)
                .orElseThrow(() -> new CourseNotFoundException(id));
        courseJpaRepository.delete(course);
    }
}
