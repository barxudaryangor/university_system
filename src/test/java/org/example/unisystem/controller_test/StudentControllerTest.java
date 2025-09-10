package org.example.unisystem.controller_test;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.unisystem.controller.StudentController;
import org.example.unisystem.dto.student.StudentDTO;
import org.example.unisystem.dto.student.StudentCreateDTO;
import org.example.unisystem.dto.student.StudentPatchDTO;
import org.example.unisystem.dto.student.StudentUpdateDTO;
import org.example.unisystem.entity.Student;
import org.example.unisystem.enums.Gender;
import org.example.unisystem.service_interface.StudentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StudentController.class)
public class StudentControllerTest {

    @MockitoBean
    StudentService studentService;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;

    @Test
    void getStudentById() throws Exception {
        StudentDTO student = new StudentDTO(
                1L, "Gor", "Barxudaryan", Gender.Male,
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
                1L, "Gor", "Barxudaryan", Gender.Male,
                LocalDate.now(), LocalDate.now(), "gmail",
                null, null
        );

        StudentDTO student2 = new StudentDTO(2L, "Ani", "Hakobyan", Gender.Female,
                LocalDate.of(2004, 5, 10), LocalDate.of(2021, 9, 1), "ani@example.com", null, null);

        when(studentService.getAllStudents()).thenReturn(List.of(student,student2));

        mockMvc.perform(get("/uni/students"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(student.getId()))
                .andExpect(jsonPath("$[0].name").value(student.getName()))
                .andExpect(jsonPath("$[0].surname").value(student.getSurname()))
                .andExpect(jsonPath("$[0].sex").value(student.getSex().toString()))
                .andExpect(jsonPath("$[0].birthdate").value(student.getBirthdate().toString()))
                .andExpect(jsonPath("$[0].enrolmentDate").value(student.getEnrolmentDate().toString()))
                .andExpect(jsonPath("$[0].email").value(student.getEmail()))
                .andExpect(jsonPath("$[1].id").value(student2.getId()))
                .andExpect(jsonPath("$[1].name").value(student2.getName()))
                .andExpect(jsonPath("$[1].surname").value(student2.getSurname()))
                .andExpect(jsonPath("$[1].sex").value(student2.getSex().toString()))
                .andExpect(jsonPath("$[1].birthdate").value(student2.getBirthdate().toString()))
                .andExpect(jsonPath("$[1].enrolmentDate").value(student2.getEnrolmentDate().toString()))
                .andExpect(jsonPath("$[1].email").value(student2.getEmail()));
        verify(studentService).getAllStudents();
    }

    @Test
    void createStudent() throws Exception {
        StudentDTO student = new StudentDTO(
                null, "Gor", "Barxudaryan", Gender.Male,
                LocalDate.of(2005, 12, 22),
                LocalDate.of(2021, 9, 1), "gor@example.com",
                null, null
        );

        StudentDTO student2 = new StudentDTO(
                1L, "Gor", "Barxudaryan", Gender.Male,
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
    void updateStudent() throws Exception {
        StudentDTO student = new StudentDTO(
                1L, "Gor", "Barxudaryan", Gender.Male,
                LocalDate.of(2005, 12, 22),
                LocalDate.of(2021, 9, 1) ,"gor@example.com",
                null, null
        );

        StudentUpdateDTO updatedStudent = new StudentUpdateDTO(
                "Garik", "Vardanyan", Gender.Male,
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
                1L, "Gor", "Barxudaryan", Gender.Male,
                LocalDate.now(), LocalDate.now(), "gmail",
                null, null
        );

        StudentDTO student2 = new StudentDTO(
                1L, "Garik", "Vardanyan", Gender.Female,
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
                1L, "Gor", "Barxudaryan", Gender.Male,
                LocalDate.now(), LocalDate.now(), "gmail",
                null, null
        );

        doNothing().when(studentService).deleteStudent(1L);

        mockMvc.perform(delete("/uni/students/" + student.getId()))
                .andExpect(status().isNoContent());

        verify(studentService).deleteStudent(1L);

    }
}
