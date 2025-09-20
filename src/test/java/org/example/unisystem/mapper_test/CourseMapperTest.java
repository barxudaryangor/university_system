package org.example.unisystem.mapper_test;

import jakarta.transaction.Transactional;
import org.example.unisystem.dto.course.CourseCreateDTO;
import org.example.unisystem.dto.course.CourseDTO;
import org.example.unisystem.entity.Course;
import org.example.unisystem.entity.Professor;
import org.example.unisystem.entity.Student;
import org.example.unisystem.entity.Assignment;
import org.example.unisystem.enums.Gender;
import org.example.unisystem.jpa_repo.AssignmentJpaRepository;
import org.example.unisystem.jpa_repo.ProfessorJpaRepository;
import org.example.unisystem.jpa_repo.StudentJpaRepository;
import org.example.unisystem.mappers.CourseMapper;
import org.example.unisystem.short_dto.AssignmentShortDTO;
import org.example.unisystem.short_dto.ProfessorShortDTO;
import org.example.unisystem.short_dto.StudentShortDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Transactional
public class CourseMapperTest {
    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private ProfessorJpaRepository professorJpaRepository;

    @Autowired
    private StudentJpaRepository studentJpaRepository;

    @Autowired
    private AssignmentJpaRepository assignmentJpaRepository;

    @Test
    void testCourseToDTO() {
        Professor professor = new Professor(1L, "John", "Smith", "CS", null);
        Student student = new Student(2L, "Alice", "Doe", Gender.FEMALE, LocalDate.now(), LocalDate.now(), "alice@mail", null, null);
        Assignment a = new Assignment(3L, "HW1", LocalDate.now().plusDays(7), null, null);

        Course course = new Course();
        course.setId(10L);
        course.setTitle("Algorithms");
        course.setCredits(5);
        course.setProfessor(professor);
        course.setStudents(Set.of(student));
        course.setAssignments(Set.of(a));

        CourseDTO dto = courseMapper.courseToDTO(course);

        assertEquals(10L, dto.getId());
        assertEquals("Algorithms", dto.getTitle());
        assertEquals(5, dto.getCredits());
        assertEquals("John", dto.getProfessor().name());
        assertEquals(1, dto.getStudents().size());
        assertEquals("Alice", dto.getStudents().get(0).name());
        assertEquals("HW1", dto.getAssignments().get(0).title());
    }

    @Test
    void testDtoToCourse() {
        Professor professorEntity = new Professor(null, "John", "Smith", "CS", null);
        professorEntity = professorJpaRepository.save(professorEntity);

        Student studentEntity = new Student(null, "Alice", "Doe", Gender.FEMALE,
                LocalDate.now(), LocalDate.now(), "mail", null, null);
        studentEntity = studentJpaRepository.save(studentEntity);

        Assignment assignmentEntity = new Assignment(null, "HW1", LocalDate.now(), null, null);
        assignmentEntity = assignmentJpaRepository.save(assignmentEntity);

        // --- 2. Создаём DTO на основе сохранённых сущностей ---
        ProfessorShortDTO professor = new ProfessorShortDTO(
                professorEntity.getId(),
                professorEntity.getName(),
                professorEntity.getSurname(),
                professorEntity.getDepartment()
        );

        StudentShortDTO sDto = new StudentShortDTO(
                studentEntity.getId(),
                studentEntity.getName(),
                studentEntity.getSurname(),
                studentEntity.getSex(),
                studentEntity.getBirthdate(),
                studentEntity.getEnrolmentDate(),
                studentEntity.getEmail()
        );

        AssignmentShortDTO aDto = new AssignmentShortDTO(
                assignmentEntity.getId(),
                assignmentEntity.getTitle(),
                assignmentEntity.getDueDate()
        );

        CourseCreateDTO courseDTO = new CourseCreateDTO(
                 "Algorithms", 5, List.of(sDto), professor, List.of(aDto)
        );

        // --- 3. Маппинг DTO → Entity ---
        Course course = courseMapper.dtoToCourse(courseDTO);

        // --- 4. Проверки ---
        assertEquals("Algorithms", course.getTitle());
        assertEquals(5, course.getCredits());

        // проверяем, что professor подтянулся из репозитория
        assertNotNull(course.getProfessor());
        assertEquals(professorEntity.getId(), course.getProfessor().getId());

        // проверяем студентов
        assertNotNull(course.getStudents());
        assertEquals(1, course.getStudents().size());
        assertEquals(studentEntity.getId(), course.getStudents().iterator().next().getId());

        // проверяем assignments
        assertNotNull(course.getAssignments());
        assertEquals(1, course.getAssignments().size());
        assertEquals(assignmentEntity.getId(), course.getAssignments().iterator().next().getId());

        // проверяем обратную связь (Assignment.course указывает на Course)
        assertEquals(course, course.getAssignments().iterator().next().getCourse());
    }

    }



