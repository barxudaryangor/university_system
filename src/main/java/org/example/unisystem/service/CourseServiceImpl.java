package org.example.unisystem.service;

import lombok.RequiredArgsConstructor;
import org.example.unisystem.dto.course.CourseCreateDTO;
import org.example.unisystem.dto.course.CourseDTO;
import org.example.unisystem.dto.course.CoursePatchDTO;
import org.example.unisystem.dto.course.CourseUpdateDTO;
import org.example.unisystem.entity.Course;
import org.example.unisystem.entity.Professor;
import org.example.unisystem.exception.course.CourseNotFoundException;
import org.example.unisystem.exception.professor.ProfessorNotFoundException;
import org.example.unisystem.jpa_repo.CourseJpaRepository;
import org.example.unisystem.jpa_repo.ProfessorJpaRepository;
import org.example.unisystem.mappers.CourseMapper;
import org.example.unisystem.pagination.PaginationResponse;
import org.example.unisystem.patch.CoursePatchApplier;
import org.example.unisystem.service_interface.CourseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CourseServiceImpl implements CourseService {

    private final CourseJpaRepository courseJpaRepository;
    private final ProfessorJpaRepository professorJpaRepository;
    private final CourseMapper courseMapper;
    private final CoursePatchApplier patchApplier;

    @Override
    public CourseDTO getCourseById(Long id) {
        Course course = courseJpaRepository.findByIdGraph(id)
                .orElseThrow(() -> new CourseNotFoundException(id));
        return courseMapper.courseToDTO(course);
    }

    @Override
    public PaginationResponse<CourseDTO> getAllCourses(Pageable pageable) {
        Page<Course> page = courseJpaRepository.findAll(pageable);
        Page<CourseDTO> response = page.map(courseMapper::courseToDTO);
        return new PaginationResponse<>(response);
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

    @Override
    @Transactional
    public CourseDTO createCourseByProfessor(Long professorId, CourseCreateDTO dto) {
        Professor professor = professorJpaRepository.findByIdGraph(professorId)
                .orElseThrow(() -> new ProfessorNotFoundException(professorId));
        Course course = courseMapper.dtoToCourse(dto);
        course.setProfessor(professor);
        return courseMapper.courseToDTO(courseJpaRepository.save(course));
    }
}
