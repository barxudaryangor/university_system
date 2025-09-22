package org.example.unisystem.controller_test;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.unisystem.controller.StudentController;
import org.example.unisystem.dto.student.StudentDTO;
import org.example.unisystem.dto.student.StudentCreateDTO;
import org.example.unisystem.dto.student.StudentPatchDTO;
import org.example.unisystem.dto.student.StudentUpdateDTO;
import org.example.unisystem.dto.submission.SubmissionCreateDTO;
import org.example.unisystem.dto.submission.SubmissionDTO;
import org.example.unisystem.entity.Student;
import org.example.unisystem.enums.Gender;
import org.example.unisystem.pagination.PaginationResponse;
import org.example.unisystem.service_interface.StudentService;
import org.example.unisystem.service_interface.SubmissionService;
import org.example.unisystem.short_dto.AssignmentShortDTO;
import org.example.unisystem.short_dto.CourseShortDTO;
import org.example.unisystem.short_dto.StudentShortDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StudentController.class)
@AutoConfigureMockMvc(addFilters = false)
public class StudentControllerTest {

    @MockitoBean
    StudentService studentService;

    @MockitoBean
    SubmissionService submissionService;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;

    @Test
    void getStudentById() throws Exception {
        StudentDTO student = new StudentDTO(
                1L, "Gor", "Barxudaryan", Gender.MALE,
                LocalDate.now(), LocalDate.now(), "gmail",
                null, null
        );

        when(studentService.getStudentById(1L)).thenReturn(student);

        mockMvc.perform(get("/uni/students/"+student.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(student.getId()))
                .andExpect(jsonPath("$.name").value(student.getName()))
                .andExpect(jsonPath("$.surname").value(student.getSurname()))
                .andExpect(jsonPath("$.sex").value(student.getSex().toString()))
                .andExpect(jsonPath("$.birthdate").value(student.getBirthdate().toString()))
                .andExpect(jsonPath("$.enrolmentDate").value(student.getEnrolmentDate().toString()))
                .andExpect(jsonPath("$.email").value(student.getEmail()));
        verify(studentService).getStudentById(any(Long.class));
    }

    @Test
    void getAllStudents() throws Exception {
        StudentDTO student = new StudentDTO(
                1L, "Gor", "Barxudaryan", Gender.MALE,
                LocalDate.now(), LocalDate.now(), "gmail",
                null, null
        );

        StudentDTO student2 = new StudentDTO(2L, "Ani", "Hakobyan", Gender.FEMALE,
                LocalDate.of(2004, 5, 10), LocalDate.of(2021, 9, 1), "ani@example.com", null, null);

        PaginationResponse<StudentDTO> response =
                new PaginationResponse<>(
                        new PageImpl<>(List.of(student, student2),
                                PageRequest.of(0, 10), 2)
                );
        when(studentService.getAllStudents(any(Pageable.class))).thenReturn(response);

        mockMvc.perform(get("/uni/students?page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(student.getId()))
                .andExpect(jsonPath("$.content[0].name").value(student.getName()))
                .andExpect(jsonPath("$.content[0].surname").value(student.getSurname()))
                .andExpect(jsonPath("$.content[0].sex").value(student.getSex().toString()))
                .andExpect(jsonPath("$.content[0].birthdate").value(student.getBirthdate().toString()))
                .andExpect(jsonPath("$.content[0].enrolmentDate").value(student.getEnrolmentDate().toString()))
                .andExpect(jsonPath("$.content[0].email").value(student.getEmail()))
                .andExpect(jsonPath("$.content[1].id").value(student2.getId()))
                .andExpect(jsonPath("$.content[1].name").value(student2.getName()))
                .andExpect(jsonPath("$.content[1].surname").value(student2.getSurname()))
                .andExpect(jsonPath("$.content[1].sex").value(student2.getSex().toString()))
                .andExpect(jsonPath("$.content[1].birthdate").value(student2.getBirthdate().toString()))
                .andExpect(jsonPath("$.content[1].enrolmentDate").value(student2.getEnrolmentDate().toString()))
                .andExpect(jsonPath("$.content[1].email").value(student2.getEmail()))
                .andExpect(jsonPath("$.totalElements").value(response.getTotalElements()))
                .andExpect(jsonPath("$.totalPages").value(response.getTotalPages()))
                .andExpect(jsonPath("$.pageNum").value(response.getPageNum()))
                .andExpect(jsonPath("$.last").value(response.isLast()))
                .andExpect(jsonPath("$.pageNum").value(0));

        verify(studentService).getAllStudents(any(Pageable.class));
    }

    @Test
    void createStudent() throws Exception {
        StudentDTO student = new StudentDTO(
                null, "Gor", "Barxudaryan", Gender.MALE,
                LocalDate.of(2005, 12, 22),
                LocalDate.of(2021, 9, 1), "gor@example.com",
                null, null
        );

        StudentDTO student2 = new StudentDTO(
                1L, "Gor", "Barxudaryan", Gender.MALE,
                student.getBirthdate(), student.getEnrolmentDate(), student.getEmail(),
                null, null
        );

        when(studentService.createStudent(any(StudentCreateDTO.class))).thenReturn(student2);

        mockMvc.perform(post("/uni/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(student)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(student2.getId()))
                .andExpect(jsonPath("$.name").value(student2.getName()))
                .andExpect(jsonPath("$.surname").value(student2.getSurname()))
                .andExpect(jsonPath("$.birthdate").value(student2.getBirthdate().toString()))
                .andExpect(jsonPath("$.enrolmentDate").value(student2.getEnrolmentDate().toString()))
                .andExpect(jsonPath("$.sex").value(student2.getSex().toString()))
                .andExpect(jsonPath("$.email").value(student2.getEmail()));
        verify(studentService).createStudent(any(StudentCreateDTO.class));
    }

    @Test
    void addStudentToCourse() throws Exception {

        CourseShortDTO courseDTO = new CourseShortDTO(
                2L, "title", 10
        );

        StudentDTO studentDTO = new StudentDTO(
                1L, "name", "surname", Gender.MALE, LocalDate.of(1010,10,10),
                LocalDate.of(80,8,8), "gor.b@gmail.com", List.of(courseDTO),  new ArrayList<>()
        );

        when(studentService.addStudentToCourse(1L,2L)).thenReturn(studentDTO);

        mockMvc.perform(post("/uni/students/1/courses/2"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.courses[0].id").value(courseDTO.id()));

        verify(studentService).addStudentToCourse(eq(1L), eq(2L));
    }

    @Test
    void submitWork() throws Exception {

        StudentShortDTO studentShortDTO = new StudentShortDTO(
                1L, "name", "surname", Gender.MALE,
                LocalDate.of(2020,10,10), LocalDate.of(2026,10,10),
                "gor@gmail.com");

        AssignmentShortDTO assignmentShortDTO = new AssignmentShortDTO(
                1L, "title", LocalDate.of(2026,10,10)
        );

        SubmissionCreateDTO createDTO = new SubmissionCreateDTO(
                LocalDate.of(2020,10,10),
                new BigDecimal(10), null, null
        );

        String request = objectMapper.writeValueAsString(createDTO);

        SubmissionDTO submissionDTO = new SubmissionDTO(
                1L, LocalDate.of(2020,10,10),
                new BigDecimal(10),studentShortDTO, assignmentShortDTO
        );

        when(submissionService.submitWork(eq(1L), eq(1L),any(SubmissionCreateDTO.class))).thenReturn(submissionDTO);

        mockMvc.perform(post("/uni/students/{studentId}/assignments/{assignmentId}/submissions", 1L, 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(request))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(submissionDTO.getId()))
                .andExpect(jsonPath("$.submittedAt").value(submissionDTO.getSubmittedAt().toString()))
                .andExpect(jsonPath("$.grade").value(submissionDTO.getGrade()))
                .andExpect(jsonPath("$.student.id").value(studentShortDTO.id()))
                .andExpect(jsonPath("$.student.name").value(studentShortDTO.name()))
                .andExpect(jsonPath("$.student.surname").value(studentShortDTO.surname()))
                .andExpect(jsonPath("$.student.sex").value(studentShortDTO.sex().toString()))
                .andExpect(jsonPath("$.student.email").value(studentShortDTO.email()))
                .andExpect(jsonPath("$.assignment.id").value(assignmentShortDTO.id()))
                .andExpect(jsonPath("$.assignment.title").value(assignmentShortDTO.title()))
                .andExpect(jsonPath("$.assignment.dueDate").value(assignmentShortDTO.dueDate().toString()));

        verify(submissionService).submitWork(eq(1L), eq(1L), any(SubmissionCreateDTO.class));
        verifyNoMoreInteractions(submissionService);

    }

    @Test
    void updateStudent() throws Exception {
        StudentDTO student = new StudentDTO(
                1L, "Gor", "Barxudaryan", Gender.MALE,
                LocalDate.of(2005, 12, 22),
                LocalDate.of(2021, 9, 1) ,"gor@example.com",
                null, null
        );

        StudentUpdateDTO updatedStudent = new StudentUpdateDTO(
                "Garik", "Vardanyan", Gender.MALE,
                LocalDate.of(2005, 12, 22),
                LocalDate.of(2021, 9, 1) ,"gor@example.com",
                null, null
        );

        doAnswer(invocation -> {
            Long id = invocation.getArgument(0);
            StudentUpdateDTO dto = invocation.getArgument(1);
            return new StudentDTO(
                    id, dto.getName(), dto.getSurname(), dto.getSex(), dto.getBirthdate(),
                    dto.getEnrolmentDate(), dto.getEmail(), null, null
            );
        }).when(studentService).updateStudent(any(Long.class), any(StudentUpdateDTO.class));


        String request = objectMapper.writeValueAsString(updatedStudent);
        mockMvc.perform(put("/uni/students/" + student.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(request))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(updatedStudent.getName()))
                .andExpect(jsonPath("$.surname").value(updatedStudent.getSurname()))
                .andExpect(jsonPath("$.sex").value(updatedStudent.getSex().toString()))
                .andExpect(jsonPath("$.birthdate").value(updatedStudent.getBirthdate().toString()))
                .andExpect(jsonPath("$.enrolmentDate").value(updatedStudent.getEnrolmentDate().toString()))
                .andExpect(jsonPath("$.email").value(updatedStudent.getEmail()));
        verify(studentService).updateStudent(any(Long.class), any(StudentUpdateDTO.class));
    }
    
    @Test
    void patchStudent() throws Exception {
        Student student = new Student(
                1L, "Gor", "Barxudaryan", Gender.MALE,
                LocalDate.now(), LocalDate.now(), "gmail",
                null, null
        );

        StudentDTO student2 = new StudentDTO(
                1L, "Garik", "Vardanyan", Gender.FEMALE,
                LocalDate.now(), LocalDate.now(), "gmail",
                null, null
        );
        
        doAnswer(invocation -> {
            Long id = invocation.getArgument(0);
            StudentPatchDTO dto = invocation.getArgument(1);
            return new StudentDTO(
                    id, dto.getName(), dto.getSurname(), dto.getSex(), dto.getBirthdate(),
                    dto.getEnrolmentDate(), dto.getEmail(), null, null
            );
        }).when(studentService).patchStudent(any(Long.class), any(StudentPatchDTO.class));
        
        String request = objectMapper.writeValueAsString(student2);
        
        mockMvc.perform(patch("/uni/students/" + student.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(request))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(student2.getId()))
                .andExpect(jsonPath("$.name").value(student2.getName()))
                .andExpect(jsonPath("$.surname").value(student2.getSurname()))
                .andExpect(jsonPath("$.sex").value(student2.getSex().toString()))
                .andExpect(jsonPath("$.birthdate").value(student2.getBirthdate().toString()))
                .andExpect(jsonPath("$.enrolmentDate").value(student2.getEnrolmentDate().toString()))
                .andExpect(jsonPath("$.email").value(student2.getEmail()));

        verify(studentService).patchStudent(eq(student.getId()), any(StudentPatchDTO.class));
    }

    @Test
    void deleteStudent() throws Exception {
        StudentDTO student = new StudentDTO(
                1L, "Gor", "Barxudaryan", Gender.MALE,
                LocalDate.now(), LocalDate.now(), "gmail",
                null, null
        );

        doNothing().when(studentService).deleteStudent(1L);

        mockMvc.perform(delete("/uni/students/" + student.getId()))
                .andExpect(status().isNoContent());

        verify(studentService).deleteStudent(1L);

    }

    @Test
    void deleteStudentFromCourse() throws Exception {
        CourseShortDTO courseDTO = new CourseShortDTO(
                2L, "title", 10
        );

        StudentDTO studentDTO = new StudentDTO(
                1L, "name", "surname", Gender.MALE, LocalDate.of(1010,10,10),
                LocalDate.of(80,8,8), "gor.b@gmail.com", List.of(courseDTO),  new ArrayList<>()
        );

        doNothing().when(studentService).deleteStudentFromCourse(1L, 2L);

        mockMvc.perform(delete("/uni/students/1/courses/2"))
                .andExpect(status().isNoContent());

        verify(studentService).deleteStudentFromCourse(eq(1L), eq(2L));
    }
}
