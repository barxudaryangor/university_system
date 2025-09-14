package org.example.unisystem.service_interface;

import org.example.unisystem.dto.student.StudentDTO;
import org.example.unisystem.dto.student.StudentCreateDTO;
import org.example.unisystem.dto.student.StudentPatchDTO;
import org.example.unisystem.dto.student.StudentUpdateDTO;
import org.example.unisystem.pagination.PaginationResponse;
import org.springframework.data.domain.Pageable;

public interface StudentService {
    StudentDTO getStudentById(Long id);
    PaginationResponse<StudentDTO> getAllStudents(Pageable pageable);
    StudentDTO createStudent(StudentCreateDTO studentDTO);
    StudentDTO updateStudent(Long id, StudentUpdateDTO studentDTO);
    StudentDTO patchStudent(Long id, StudentPatchDTO studentDTO);
    void deleteStudent(Long id);
    StudentDTO addStudentToCourse(Long studentId, Long courseId);
    void deleteStudentFromCourse(Long studentId, Long courseId);
}
