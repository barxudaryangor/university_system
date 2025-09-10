package org.example.unisystem.service_test;

import org.example.unisystem.dto.course.CourseCreateDTO;
import org.example.unisystem.dto.course.CourseDTO;
import org.example.unisystem.dto.course.CoursePatchDTO;
import org.example.unisystem.dto.course.CourseUpdateDTO;
import org.example.unisystem.entity.Course;
import org.example.unisystem.jpa_repo.CourseJpaRepository;
import org.example.unisystem.mappers.CourseMapper;
import org.example.unisystem.patch.CoursePatchApplier;
import org.example.unisystem.service.CourseServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CourseServiceTest {
    @Mock
    CourseJpaRepository courseJpaRepository;

    @Mock
    CourseMapper courseMapper;

    @Mock
    CoursePatchApplier coursePatchApplier;

    @InjectMocks
    CourseServiceImpl courseService;

    @Test
    void getCourseById() {
        Course course = new Course(
                1L, "Title", 10, null, null, null
        );

        when(courseJpaRepository.findByIdGraph(1L)).thenReturn(Optional.of(course));

        when(courseMapper.courseToDTO(course)).thenReturn(
                new CourseDTO(
                        course.getId(),
                        course.getTitle(),
                        course.getCredits(),
                        null, null, null
                )
        );

        CourseDTO dto = courseService.getCourseById(1L);
        assertEquals(course.getId(), dto.getId());
        assertEquals(course.getTitle(), dto.getTitle());
        assertEquals(course.getCredits(), dto.getCredits());
    }

    @Test
    void getAllCourses() {
        Course course = new Course(
                1L, "Title", 10, null, null, null
        );


        when(courseMapper.courseToDTO(course)).thenReturn(
                new CourseDTO(
                        course.getId(),
                        course.getTitle(),
                        course.getCredits(),
                        null, null, null
                )
        );

        Course course2 = new Course(
                2L, "Title2", 20, null, null, null
        );


        when(courseMapper.courseToDTO(course2)).thenReturn(
                new CourseDTO(
                        course2.getId(),
                        course2.getTitle(),
                        course2.getCredits(),
                        null,null, null
                )
        );

        when(courseJpaRepository.findAll()).thenReturn(List.of(course,course2));

        List<CourseDTO> courses = courseService.getAllCourses();

        assertEquals(courses.get(0).getId(), course.getId());
        assertEquals(courses.get(0).getTitle(), course.getTitle());
        assertEquals(courses.get(0).getCredits(), course.getCredits());
        assertEquals(courses.get(1).getId(), course2.getId());
        assertEquals(courses.get(1).getTitle(), course2.getTitle());
        assertEquals(courses.get(1).getCredits(), course2.getCredits());

    }

    @Test
    void createCourse() {
        CourseCreateDTO request = new CourseCreateDTO(
                "Title", 10, null, null, null
        );

        Course entity = new Course(
                null, "Title", 10, null, null, null
        );

        Course savedEntity = new Course(
                1L, "Title", 10, null, null, null
        );

        CourseDTO response = new CourseDTO(
                1L, "Title", 10, null, null, null
        );

        when(courseMapper.dtoToCourse(request)).thenReturn(entity);
        when(courseJpaRepository.save(entity)).thenReturn(savedEntity);
        when(courseMapper.courseToDTO(savedEntity)).thenReturn(response);

        CourseDTO dto = courseService.createCourse(request);

        assertEquals(request.getTitle(), dto.getTitle());
        assertEquals(request.getCredits(), dto.getCredits());

        verify(courseJpaRepository).save(entity);
    }

    @Test
    void updateCourse() {

        Course course = new Course(
                1L, "Title2", 20, null, null, null
        );

        CourseUpdateDTO updateDTO = new CourseUpdateDTO(
                "Title", 10, null, null, null
        );

        Course updatedDTO = new Course(
                1L, "Title", 10, null, null, null
        );

        CourseDTO response = new CourseDTO(
                1L, "Title", 10, null, null, null
        );

        doAnswer(invocation -> {
            CourseUpdateDTO dto = invocation.getArgument(0);
            Course c = invocation.getArgument(1);
            c.setTitle(dto.getTitle());
            c.setCredits(dto.getCredits());
            return null;
        }).when(courseMapper).updateCourseFromDTO(any(CourseUpdateDTO.class), any(Course.class));

        when(courseJpaRepository.findByIdGraph(1L)).thenReturn(Optional.of(course));
        when(courseMapper.courseToDTO(course)).thenReturn(response);

        CourseDTO dto = courseService.updateCourse(1L, updateDTO);
        assertEquals(response.getTitle(), dto.getTitle());
        assertEquals(response.getCredits(), dto.getCredits());
        assertEquals(response.getId(), dto.getId());



    }

    @Test
    void patchCourse() {
        Course course = new Course(
                1L, "Title", 10, null, null,null
        );

        CoursePatchDTO patchDTO = new CoursePatchDTO(
                null, 20, null, null, null
        );

        Course patchedCourse = new Course(
                1L, "Title", 20, null, null, null
        );

        CourseDTO returnedCourse = new CourseDTO(
                1L, "Title", 20, null, null, null
        );

        when(courseJpaRepository.findByIdGraph(1L)).thenReturn(Optional.of(course));

        doAnswer(invocation -> {
            Course c = invocation.getArgument(0);
            CoursePatchDTO p = invocation.getArgument(1);
            if(p.getTitle() != null) c.setTitle(p.getTitle());
            if(p.getCredits() != null) c.setCredits(p.getCredits());
            return null;
        }).when(coursePatchApplier).patchCourse(any(Course.class), any(CoursePatchDTO.class));

        when(courseJpaRepository.save(course)).thenReturn(patchedCourse);
        when(courseMapper.courseToDTO(patchedCourse)).thenReturn(returnedCourse);

        CourseDTO dto = courseService.patchCourse(1L, patchDTO);

        assertEquals(1L, dto.getId());
        assertEquals(returnedCourse.getTitle(), dto.getTitle());
        assertEquals(returnedCourse.getCredits(), dto.getCredits());

        verify(coursePatchApplier).patchCourse(eq(course), eq(patchDTO));

    }

    @Test
    void deleteCourse() {
        Course course = new Course(
                1L, "Title", 10, null, null, null
        );

        when(courseJpaRepository.findByIdGraph(1L)).thenReturn(Optional.of(course));
        courseService.deleteCourse(1L);

        verify(courseJpaRepository).findByIdGraph(1L);
        verify(courseJpaRepository).delete(course);
        verifyNoMoreInteractions(courseJpaRepository);

    }
}
