package org.example.unisystem.service_test;

import org.example.unisystem.dto.student.StudentDTO;
import org.example.unisystem.dto.student.StudentCreateDTO;
import org.example.unisystem.dto.student.StudentPatchDTO;
import org.example.unisystem.dto.student.StudentUpdateDTO;
import org.example.unisystem.entity.Course;
import org.example.unisystem.entity.Student;
import org.example.unisystem.enums.Gender;
import org.example.unisystem.jpa_repo.CourseJpaRepository;
import org.example.unisystem.jpa_repo.StudentJpaRepository;
import org.example.unisystem.mappers.StudentMapper;
import org.example.unisystem.pagination.PaginationResponse;
import org.example.unisystem.patch.StudentPatchApplier;
import org.example.unisystem.service.StudentServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {
    @Mock
    StudentJpaRepository studentJpaRepository;

    @Mock
    CourseJpaRepository courseJpaRepository;

    @Mock
    StudentMapper studentMapper;

    @Mock
    StudentPatchApplier studentPatch;

    @InjectMocks
    StudentServiceImpl studentService;

    @Test
    void getStudentById() {
        Student student = new Student(
                1L, "Ani", "Hakobyan", Gender.Female,
                LocalDate.of(2004, 5, 10),
                LocalDate.of(2021, 9, 1),
                "ani@example.com", new HashSet<>(), new HashSet<>()
        );

        when(studentJpaRepository.findByIdGraph(1L)).thenReturn(Optional.of(student));

        when(studentMapper.studentToDTO(student)).thenReturn(
                new StudentDTO(
                        student.getId(),
                        student.getName(),
                        student.getSurname(),
                        student.getSex(),
                        student.getBirthdate(),
                        student.getEnrolmentDate(),
                        student.getEmail(),
                        null,
                        null
                )
        );

        StudentDTO result = studentService.getStudentById(1L);
        assertEquals(1L, result.getId());
        assertEquals("Ani", result.getName());
        assertEquals("Hakobyan", result.getSurname());
        assertEquals("ani@example.com", result.getEmail());
    }

    @Test
    void getAllStudents(){
        Student student = new Student(
                1L, "Ani", "Hakobyan", Gender.Female,
                LocalDate.of(2004, 5, 10),
                LocalDate.of(2021, 9, 1),
                "ani@example.com", new HashSet<>(), new HashSet<>()
        );

        Student student2 = new Student(2L, "Alice", "Doe", Gender.Female,
                LocalDate.now(), LocalDate.now(), "mail", new HashSet<>(), new HashSet<>());

        Page<Student> page = new PageImpl<>(List.of(student,student2), PageRequest.of(0,10), 2);
        when(studentJpaRepository.findAll(any(Pageable.class))).thenReturn(page);

        when(studentMapper.studentToDTO(student)).thenReturn(
                new StudentDTO(
                        student.getId(),
                        student.getName(),
                        student.getSurname(),
                        student.getSex(),
                        student.getBirthdate(),
                        student.getEnrolmentDate(),
                        student.getEmail(),
                        null,
                        null
                )
        );

        when(studentMapper.studentToDTO(student2)).thenReturn(
                new StudentDTO(
                        student2.getId(),
                        student2.getName(),
                        student2.getSurname(),
                        student2.getSex(),
                        student2.getBirthdate(),
                        student2.getEnrolmentDate(),
                        student2.getEmail(),
                        null,
                        null
                )
        );

        PaginationResponse<StudentDTO> response =
                studentService.getAllStudents(PageRequest.of(0, 10));
        List<StudentDTO> students = response.getContent();

        assertEquals(students.getFirst().getId(), student.getId());
        assertEquals(students.getFirst().getName(), student.getName());
        assertEquals(students.getFirst().getEmail(), student.getEmail());
        assertEquals(students.getFirst().getSex(), student.getSex());
        assertEquals(students.getFirst().getEnrolmentDate(), student.getEnrolmentDate());
        assertEquals(students.get(0).getBirthdate(), student.getBirthdate());
        assertEquals(students.get(0).getSurname(), student.getSurname());

        assertEquals(students.get(1).getId(), student2.getId());
        assertEquals(students.get(1).getName(), student2.getName());
        assertEquals(students.get(1).getEmail(), student2.getEmail());
        assertEquals(students.get(1).getSex(), student2.getSex());
        assertEquals(students.get(1).getEnrolmentDate(), student2.getEnrolmentDate());
        assertEquals(students.get(1).getBirthdate(), student2.getBirthdate());
        assertEquals(students.get(1).getSurname(), student2.getSurname());

        assertEquals(0, response.getPageNum());
        assertEquals(10, response.getPageSize());
        assertEquals(2, response.getTotalElements());
        assertEquals(1, response.getTotalPages());
        assertEquals(true, response.isLast());



        verify(studentJpaRepository).findAll(any(Pageable.class));
    }

    @Test
    void createStudent() {

        StudentCreateDTO request = new StudentCreateDTO(
                "Gor", "Barxudaryan", Gender.Male, LocalDate.now(), LocalDate.now(), "email", null,null
        );
        Student entity = new Student(
                null, "Gor", "Barxudaryan", Gender.Male, LocalDate.now(), LocalDate.now(), "email", new HashSet<>(), new HashSet<>()
        );

        Student savedEntity = new Student(
                1L, "Gor", "Barxudaryan", Gender.Male, LocalDate.now(), LocalDate.now(), "email", new HashSet<>(), new HashSet<>()
        );

        StudentDTO response = new StudentDTO(
                1L, "Gor", "Barxudaryan", Gender.Male, LocalDate.now(), LocalDate.now(), "email", null,null
        );

        when(studentMapper.dtoToStudent(request)).thenReturn(entity);
        when(studentJpaRepository.save(entity)).thenReturn(savedEntity);
        when(studentMapper.studentToDTO(savedEntity)).thenReturn(response);

        StudentDTO result = studentService.createStudent(request);

        assertEquals(response.getId(), result.getId());
        assertEquals(response.getName(), result.getName());
        assertEquals(response.getSurname(), result.getSurname());
        assertEquals(response.getSex(), result.getSex());
        assertEquals(response.getBirthdate(), result.getEnrolmentDate());
        assertEquals(response.getEnrolmentDate(), result.getEnrolmentDate());
        assertEquals(response.getCourses(), result.getCourses());
        assertEquals(response.getSubmissions(), result.getSubmissions());

    }

    @Test
    void updateStudent() {
        Student student = new Student(
                1L, "Ani", "Hakobyan", Gender.Female,
                LocalDate.of(2004, 5, 10),
                LocalDate.of(2021, 9, 1),
                "ani@example.com", new HashSet<>(), new HashSet<>()
        );



        StudentUpdateDTO updateDto = new StudentUpdateDTO(
                "Gor", "Barxudaryan", Gender.Male,
                LocalDate.of(2005, 12, 22),
                LocalDate.of(2025, 9, 1),
                "gor@example.com", null, null
        );

        Student updatedEntity = new Student(
                1L, "Gor", "Barxudaryan", Gender.Male,
                LocalDate.of(2005, 12, 22),
                LocalDate.of(2025, 9, 1),
                "gor@example.com", new HashSet<>(), new HashSet<>()
        );

        StudentDTO updatedDTO = new StudentDTO(
                1L, "Gor", "Barxudaryan", Gender.Male,
                LocalDate.of(2005, 12, 22),
                LocalDate.of(2025, 9, 1),
                "gor@example.com", null, null
        );

        when(studentJpaRepository.findByIdGraph(1L)).thenReturn(Optional.of(student));

        doAnswer(invocation -> {
            StudentUpdateDTO dto = invocation.getArgument(0);
            Student target = invocation.getArgument(1);
            target.setName(dto.getName());
            target.setSurname(dto.getSurname());
            target.setSex(dto.getSex());
            target.setBirthdate(dto.getBirthdate());
            target.setEnrolmentDate(dto.getEnrolmentDate());
            target.setEmail(dto.getEmail());
            return null;
        }).when(studentMapper).updateStudentFromDto(updateDto, student);

        when(studentJpaRepository.save(student)).thenReturn(updatedEntity);
        when(studentMapper.studentToDTO(updatedEntity)).thenReturn(updatedDTO);
        StudentDTO result = studentService.updateStudent(1L, updateDto);

        assertEquals("Gor", result.getName());
        assertEquals("Barxudaryan", result.getSurname());
        assertEquals("gor@example.com", result.getEmail());

        verify(studentJpaRepository).save(student);
    }

    @Test
    void patchStudent() {
        Student student = new Student(
                1L, "Ani", "Hakobyan", Gender.Female,
                LocalDate.of(2004, 5, 10),
                LocalDate.of(2021, 9, 1),
                "ani@example.com", new HashSet<>(), new HashSet<>()
        );

        StudentPatchDTO updateDto = new StudentPatchDTO(
                "Garik", "Barxudaryan", Gender.Male,
                LocalDate.of(2005, 12, 22),
                LocalDate.of(2025, 9, 1),
                "garik@example.com", null, null
        );

        when(studentJpaRepository.findByIdGraph(1L)).thenReturn(Optional.of(student));

        doAnswer(invocation -> {
            Student s = invocation.getArgument(0);
            StudentPatchDTO dto = invocation.getArgument(1);
            s.setName(dto.getName());
            s.setSurname(dto.getSurname());
            s.setSex(dto.getSex());
            s.setBirthdate(dto.getBirthdate());
            s.setEnrolmentDate(dto.getEnrolmentDate());
            s.setEmail(dto.getEmail());
            return null;
        }).when(studentPatch).patchStudent(any(Student.class), any(StudentPatchDTO.class));

        StudentDTO responseDTO = new StudentDTO(
                1L, "Garik", "Barxudaryan", Gender.Male,
                LocalDate.of(2005, 12, 22),
                LocalDate.of(2025, 9, 1),
                "garik@example.com", null, null
        );

        when(studentJpaRepository.save(student)).thenReturn(student);
        when(studentMapper.studentToDTO(student)).thenReturn(responseDTO);

        StudentDTO patchedStudent = studentService.patchStudent(1L, updateDto);

        assertEquals(patchedStudent.getId(), responseDTO.getId());
        assertEquals(patchedStudent.getName(), responseDTO.getName());
        assertEquals(patchedStudent.getSurname(), responseDTO.getSurname());
        assertEquals(patchedStudent.getSex(), responseDTO.getSex());
        assertEquals(patchedStudent.getBirthdate(), responseDTO.getBirthdate());
        assertEquals(patchedStudent.getEnrolmentDate(), responseDTO.getEnrolmentDate());
        assertEquals(patchedStudent.getEmail(), responseDTO.getEmail());

        verify(studentPatch).patchStudent(eq(student), eq(updateDto));
    }

    @Test
    void addStudentToCourse() {
        Student student = new Student(
                1L, "name", "surname", Gender.Male, LocalDate.of(10,10,10),
                LocalDate.of(8,8,8), "gor@gmai.com", new HashSet<>() , new HashSet<>()
        );

        StudentDTO studentDTO = new StudentDTO(
                1L, "name", "surname", Gender.Male, LocalDate.of(10,10,10),
                LocalDate.of(8,8,8), "gor@gmai.com", new ArrayList<>(), new ArrayList<>()
        );

        Course course = new Course(
                2L, "title", 10, new HashSet<>(), null, new HashSet<>()
        );

        when(studentJpaRepository.findByIdGraph(1L)).thenReturn(Optional.of(student));
        when(courseJpaRepository.findByIdGraph(2L)).thenReturn(Optional.of(course));
        when(studentMapper.studentToDTO(student)).thenReturn(studentDTO);

        StudentDTO addedStudent = studentService.addStudentToCourse(1L, 2L);

        assertTrue(student.getCourses().contains(course));
        assertEquals(1, student.getCourses().size());
        assertSame(studentDTO, addedStudent);

        verify(studentJpaRepository).findByIdGraph(1L);
        verify(courseJpaRepository).findByIdGraph(2L);
        verify(studentMapper).studentToDTO(student);
    }

    @Test
    void deleteStudent() {
        Student student = new Student(
                1L, "Ani", "Hakobyan", Gender.Female,
                LocalDate.of(2004, 5, 10),
                LocalDate.of(2021, 9, 1),
                "ani@example.com", new HashSet<>(), new HashSet<>()
        );

        when(studentJpaRepository.findByIdGraph(1L)).thenReturn(Optional.of(student));

        studentService.deleteStudent(1L);

        verify(studentJpaRepository).delete(student);
        verify(studentJpaRepository).findByIdGraph(1L);
        verifyNoMoreInteractions(studentJpaRepository);
    }

    @Test
    void deleteStudentFromCourse() {

        Course course = new Course(
                2L, "title", 10, new HashSet<>(), null, new HashSet<>()
        );

        Student student = new Student(
                1L, "name", "surname", Gender.Male, LocalDate.of(10,10,10),
                LocalDate.of(8,8,8), "gor@gmai.com", new HashSet<>(Set.of(course)) , new HashSet<>()
        );

        course.getStudents().add(student);

        when(studentJpaRepository.findByIdGraph(1L)).thenReturn(Optional.of(student));
        when(courseJpaRepository.findByIdGraph(2L)).thenReturn(Optional.of(course));

        studentService.deleteStudentFromCourse(1L, 2L);

        assertFalse(student.getCourses().contains(course));
        verify(studentJpaRepository).findByIdGraph(1L);
        verify(courseJpaRepository).findByIdGraph(2L);

    }
}
