package org.example.unisystem.service_interface;

import jakarta.annotation.Nullable;
import org.example.unisystem.dto.assignment.AssignmentCreateDTO;
import org.example.unisystem.dto.assignment.AssignmentDTO;
import org.example.unisystem.dto.assignment.AssignmentPatchDTO;
import org.example.unisystem.dto.assignment.AssignmentUpdateDTO;
import org.example.unisystem.pagination.PaginationResponse;
import org.springframework.data.domain.Pageable;

public interface AssignmentService {
    AssignmentDTO getAssignmentById(Long id);
    PaginationResponse<AssignmentDTO> getAllAssignments(Pageable pageable);
    AssignmentDTO createAssignment(AssignmentCreateDTO assignmentDTO);
    AssignmentDTO updateAssignment(Long id, AssignmentUpdateDTO assignmentDTO);
    AssignmentDTO patchAssignment(Long id, AssignmentPatchDTO assignmentDTO);
    void deleteAssignment(Long id);
    AssignmentDTO createAssignmentForCourse(@Nullable Long professorId, Long courseId, AssignmentCreateDTO dto);
}
