package org.example.unisystem.controller;

import org.example.unisystem.dto.assignment.AssignmentCreateDTO;
import org.example.unisystem.dto.assignment.AssignmentDTO;
import org.example.unisystem.dto.assignment.AssignmentPatchDTO;
import org.example.unisystem.dto.assignment.AssignmentUpdateDTO;
import org.example.unisystem.pagination.PaginationResponse;
import org.example.unisystem.service_interface.AssignmentService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
    PaginationResponse<AssignmentDTO> getAllAssignments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page,size);
        return assignmentService.getAllAssignments(pageable);
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
