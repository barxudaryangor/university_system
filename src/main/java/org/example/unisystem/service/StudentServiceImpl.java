package org.example.unisystem.service;

import org.example.unisystem.dto.student.StudentCreateDTO;
import org.example.unisystem.dto.student.StudentPatchDTO;
import org.example.unisystem.dto.student.StudentUpdateDTO;
import org.example.unisystem.exception.student.StudentNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.unisystem.dto.student.StudentDTO;
import org.example.unisystem.entity.Student;
import org.example.unisystem.jpa_repo.StudentJpaRepository;
import org.example.unisystem.mappers.StudentMapper;
import org.example.unisystem.patch.StudentPatchApplier;
import org.example.unisystem.service_interface.StudentService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StudentServiceImpl implements StudentService {

    private final StudentJpaRepository studentJpaRepository;
    private final StudentMapper studentMapper;
    private final StudentPatchApplier studentPatch;

    @Override
    public StudentDTO getStudentById(Long id) {
        Student student = studentJpaRepository.findByIdGraph(id)
                .orElseThrow(() -> new StudentNotFoundException(id));
        return studentMapper.studentToDTO(student);
    }

    @Override
    public List<StudentDTO> getAllStudents() {
        return studentJpaRepository.findAll()
                .stream().map(studentMapper::studentToDTO)
                .toList();
    }

    @Override
    @Transactional
    public StudentDTO createStudent(StudentCreateDTO studentDTO) {
        Student student = studentMapper.dtoToStudent(studentDTO);
        return studentMapper.studentToDTO(studentJpaRepository.save(student));
    }

    @Override
    @Transactional
    public StudentDTO updateStudent(Long id, StudentUpdateDTO studentDTO) {
        Student student = studentJpaRepository.findByIdGraph(id)
                .orElseThrow(() -> new StudentNotFoundException(id));
        studentMapper.updateStudentFromDto(studentDTO, student);
        return studentMapper.studentToDTO(studentJpaRepository.save(student));
    }

    @Override
    @Transactional
    public StudentDTO patchStudent(Long id, StudentPatchDTO studentDTO) {
        Student s = studentJpaRepository.findByIdGraph(id)
                .orElseThrow(() -> new StudentNotFoundException(id));
        studentPatch.patchStudent(s, studentDTO);
        return studentMapper.studentToDTO(studentJpaRepository.save(s));
    }

    @Override
    @Transactional
    public void deleteStudent(Long id) {
        Student student = studentJpaRepository.findByIdGraph(id)
                .orElseThrow(() -> new StudentNotFoundException(id));
        studentJpaRepository.delete(student);
    }
}
