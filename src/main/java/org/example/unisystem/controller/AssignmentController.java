package org.example.unisystem.controller;

import org.apache.coyote.Response;
import org.example.unisystem.dto.assignment.AssignmentCreateDTO;
import org.example.unisystem.dto.assignment.AssignmentDTO;
import org.example.unisystem.dto.assignment.AssignmentPatchDTO;
import org.example.unisystem.dto.assignment.AssignmentUpdateDTO;
import org.example.unisystem.service_interface.AssignmentService;
import org.springframework.expression.spel.ast.Assign;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/uni/assignments")
@Validated
public class AssignmentController {
    private final AssignmentService assignmentService;

    public AssignmentController(AssignmentService assignmentService) {
        this.assignmentService = assignmentService;
    }

    @GetMapping("/{id}")
    AssignmentDTO getAssignmentById(@PathVariable Long id) {
        return assignmentService.getAssignmentById(id);
    }

    @GetMapping
    List<AssignmentDTO> getAllAssignments() {
        return assignmentService.getAllAssignments();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    AssignmentDTO createAssignment(@Validated @RequestBody AssignmentCreateDTO assignmentDTO) {
        return assignmentService.createAssignment(assignmentDTO);
    }

    @PutMapping("/{id}")
    AssignmentDTO updateAssignment(@PathVariable Long id, @Validated @RequestBody AssignmentUpdateDTO assignmentUpdateDTO) {
        return assignmentService.updateAssignment(id, assignmentUpdateDTO);
    }

    @PatchMapping("/{id}")
    AssignmentDTO patchAssignment (@PathVariable Long id, @Validated @RequestBody AssignmentPatchDTO assignmentPatchDTO) {
        return assignmentService.patchAssignment(id, assignmentPatchDTO);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        assignmentService.deleteAssignment(id);
        return ResponseEntity.noContent().build();
    }

}
