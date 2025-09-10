package org.example.unisystem.service;

import lombok.RequiredArgsConstructor;
import org.example.unisystem.dto.assignment.AssignmentCreateDTO;
import org.example.unisystem.dto.assignment.AssignmentDTO;
import org.example.unisystem.dto.assignment.AssignmentPatchDTO;
import org.example.unisystem.dto.assignment.AssignmentUpdateDTO;
import org.example.unisystem.entity.Assignment;
import org.example.unisystem.exception.assignment.AssignmentNotFoundException;
import org.example.unisystem.jpa_repo.AssignmentJpaRepository;
import org.example.unisystem.mappers.AssignmentMapper;
import org.example.unisystem.patch.AssignmentPatchApplier;
import org.example.unisystem.service_interface.AssignmentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AssignmentServiceImpl implements AssignmentService {

    private final AssignmentJpaRepository assignmentJpaRepository;
    private final AssignmentMapper assignmentMapper;
    private final AssignmentPatchApplier assignmentPatchApplier;

    @Override
    public AssignmentDTO getAssignmentById(Long id) {
        Assignment assignment = assignmentJpaRepository.findByIdGraph(id)
                .orElseThrow(() -> new AssignmentNotFoundException(id));
        return assignmentMapper.assignmentToDTO(assignment);
    }

    @Override
    public List<AssignmentDTO> getAllAssignments() {
        return assignmentJpaRepository.findAll().stream()
                .map(assignmentMapper::assignmentToDTO)
                .toList();
    }

    @Override
    @Transactional
    public AssignmentDTO createAssignment(AssignmentCreateDTO assignmentDTO) {
        Assignment assignment = assignmentMapper.dtoToAssignment(assignmentDTO);
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
