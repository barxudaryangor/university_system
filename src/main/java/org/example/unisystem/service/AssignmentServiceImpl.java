package org.example.unisystem.service;

import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.example.unisystem.dto.assignment.AssignmentCreateDTO;
import org.example.unisystem.dto.assignment.AssignmentDTO;
import org.example.unisystem.dto.assignment.AssignmentPatchDTO;
import org.example.unisystem.dto.assignment.AssignmentUpdateDTO;
import org.example.unisystem.entity.Assignment;
import org.example.unisystem.entity.Course;
import org.example.unisystem.exception.assignment.AssignmentNotFoundException;
import org.example.unisystem.exception.course.CourseNotFoundException;
import org.example.unisystem.jpa_repo.AssignmentJpaRepository;
import org.example.unisystem.jpa_repo.CourseJpaRepository;
import org.example.unisystem.mappers.AssignmentMapper;
import org.example.unisystem.pagination.PaginationResponse;
import org.example.unisystem.patch.AssignmentPatchApplier;
import org.example.unisystem.service_interface.AssignmentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AssignmentServiceImpl implements AssignmentService {

    private final AssignmentJpaRepository assignmentJpaRepository;
    private final CourseJpaRepository courseJpaRepository;
    private final AssignmentMapper assignmentMapper;
    private final AssignmentPatchApplier assignmentPatchApplier;

    @Override
    public AssignmentDTO getAssignmentById(Long id) {
        Assignment assignment = assignmentJpaRepository.findByIdGraph(id)
                .orElseThrow(() -> new AssignmentNotFoundException(id));
        return assignmentMapper.assignmentToDTO(assignment);
    }

    @Override
    public PaginationResponse<AssignmentDTO> getAllAssignments(Pageable pageable) {
        Page<Assignment> page = assignmentJpaRepository.findAll(pageable);
        Page<AssignmentDTO> response = page.map(assignmentMapper::assignmentToDTO);
        return new PaginationResponse<>(response);
    }

    @Override
    @Transactional
    public AssignmentDTO createAssignment(AssignmentCreateDTO assignmentDTO) {
        Assignment assignment = assignmentMapper.dtoToAssignment(assignmentDTO);
        return assignmentMapper.assignmentToDTO(assignmentJpaRepository.save(assignment));
    }


    @Override
    @Transactional
    public AssignmentDTO createAssignmentForCourse(@Nullable Long professorId, Long courseId, AssignmentCreateDTO dto) {
        Course course = courseJpaRepository.findByIdGraph(courseId)
                .orElseThrow(() -> new CourseNotFoundException(courseId));

        if(professorId != null) {
            if(course.getProfessor() == null || !course.getProfessor().getId().equals(professorId)) {
                throw new IllegalArgumentException("professor.with.id." + professorId +
                        ".doesn't.own.course.with.id." + courseId);
            }
        }

        Assignment assignment = assignmentMapper.dtoToAssignment(dto);
        assignment.setCourse(course);
        return assignmentMapper.assignmentToDTO(assignmentJpaRepository.save(assignment));
    }

    @Override
    @Transactional
    public AssignmentDTO updateAssignment(Long id, AssignmentUpdateDTO assignmentDTO) {
        Assignment assignment = assignmentJpaRepository.findByIdGraph(id)
                .orElseThrow(() -> new AssignmentNotFoundException(id));
        assignmentMapper.updateAssignmentFromDTO(assignmentDTO, assignment);
        return assignmentMapper.assignmentToDTO(assignmentJpaRepository.save(assignment));
    }

    @Override
    @Transactional
    public AssignmentDTO patchAssignment(Long id, AssignmentPatchDTO assignmentDTO) {
        Assignment assignment = assignmentJpaRepository.findByIdGraph(id)
                .orElseThrow( () -> new AssignmentNotFoundException(id));
        assignmentPatchApplier.patchAssignment(assignment, assignmentDTO);
        return assignmentMapper.assignmentToDTO(assignmentJpaRepository.save(assignment));
    }

    @Override
    @Transactional
    public void deleteAssignment(Long id) {
        Assignment assignment = assignmentJpaRepository.findByIdGraph(id)
                .orElseThrow( () -> new AssignmentNotFoundException(id));
        assignmentJpaRepository.delete(assignment);
    }


}
