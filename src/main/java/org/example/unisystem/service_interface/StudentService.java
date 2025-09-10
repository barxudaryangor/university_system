package org.example.unisystem.service_interface;

import org.example.unisystem.dto.student.StudentDTO;
import org.example.unisystem.dto.student.StudentCreateDTO;
import org.example.unisystem.dto.student.StudentPatchDTO;
import org.example.unisystem.dto.student.StudentUpdateDTO;

import java.util.List;

public interface StudentService {
    StudentDTO getStudentById(Long id);
    List<StudentDTO> getAllStudents();
    StudentDTO createStudent(StudentCreateDTO studentDTO);
    StudentDTO updateStudent(Long id, StudentUpdateDTO studentDTO);
    StudentDTO patchStudent(Long id, StudentPatchDTO studentDTO);
    void deleteStudent(Long id);
}
