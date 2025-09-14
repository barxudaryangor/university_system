package org.example.unisystem.service;

import org.example.unisystem.dto.student.StudentCreateDTO;
import org.example.unisystem.dto.student.StudentPatchDTO;
import org.example.unisystem.dto.student.StudentUpdateDTO;
import org.example.unisystem.entity.Course;
import org.example.unisystem.exception.course.CourseNotFoundException;
import org.example.unisystem.exception.student.StudentNotFoundException;
import org.example.unisystem.jpa_repo.CourseJpaRepository;
import org.example.unisystem.pagination.PaginationResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.unisystem.dto.student.StudentDTO;
import org.example.unisystem.entity.Student;
import org.example.unisystem.jpa_repo.StudentJpaRepository;
import org.example.unisystem.mappers.StudentMapper;
import org.example.unisystem.patch.StudentPatchApplier;
import org.example.unisystem.service_interface.StudentService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StudentServiceImpl implements StudentService {

    private final StudentJpaRepository studentJpaRepository;
    private final CourseJpaRepository courseJpaRepository;
    private final StudentMapper studentMapper;
    private final StudentPatchApplier studentPatch;

    @Override
    public StudentDTO getStudentById(Long id) {
        Student student = studentJpaRepository.findByIdGraph(id)
                .orElseThrow(() -> new StudentNotFoundException(id));
        return studentMapper.studentToDTO(student);
    }


    @Override
    public PaginationResponse<StudentDTO> getAllStudents(Pageable pageable) {
        Page<Student> page = studentJpaRepository.findAll(pageable);
        Page<StudentDTO> response = page.map(studentMapper::studentToDTO);
        return new PaginationResponse<>(response);
    }

    @Override
    @Transactional
    public StudentDTO createStudent(StudentCreateDTO studentDTO) {
        Student student = studentMapper.dtoToStudent(studentDTO);
        return studentMapper.studentToDTO(studentJpaRepository.save(student));
    }

    @Override
    @Transactional
    public StudentDTO addStudentToCourse(Long studentId, Long courseId) {
        Student student = studentJpaRepository.findByIdGraph(studentId)
                .orElseThrow(() -> new StudentNotFoundException(studentId));
        Course course = courseJpaRepository.findByIdGraph(courseId)
                .orElseThrow(() -> new CourseNotFoundException(courseId));

        student.getCourses().add(course);

        return studentMapper.studentToDTO(student);
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

    @Override
    @Transactional
    public void deleteStudentFromCourse(Long studentId, Long courseId) {
        Student student = studentJpaRepository.findByIdGraph(studentId)
                .orElseThrow(() -> new StudentNotFoundException(studentId));
        Course course = courseJpaRepository.findByIdGraph(courseId)
                .orElseThrow(() -> new CourseNotFoundException(courseId));

        if(!student.getCourses().contains(course)) {
            throw new IllegalArgumentException("student.with.id." + studentId + ".is.not.enrolled.in.course"+
                   "with.id." + courseId);
        }
        student.getCourses().remove(course);
    }


}
