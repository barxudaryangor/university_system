package org.example.unisystem.service_interface;

import org.example.unisystem.dto.assignment.AssignmentCreateDTO;
import org.example.unisystem.dto.assignment.AssignmentDTO;
import org.example.unisystem.dto.assignment.AssignmentPatchDTO;
import org.example.unisystem.dto.assignment.AssignmentUpdateDTO;

import java.util.List;

public interface AssignmentService {
    AssignmentDTO getAssignmentById(Long id);
    List<AssignmentDTO> getAllAssignments();
    AssignmentDTO createAssignment(AssignmentCreateDTO assignmentDTO);
    AssignmentDTO updateAssignment(Long id, AssignmentUpdateDTO assignmentDTO);
    AssignmentDTO patchAssignment(Long id, AssignmentPatchDTO assignmentDTO);
    void deleteAssignment(Long id);
}
