package org.example.unisystem.ui;

import jakarta.validation.Valid;
import org.example.unisystem.dto.assignment.AssignmentCreateDTO;
import org.example.unisystem.dto.assignment.AssignmentDTO;
import org.example.unisystem.dto.assignment.AssignmentPatchDTO;
import org.example.unisystem.dto.assignment.AssignmentUpdateDTO;
import org.example.unisystem.pagination.PaginationResponse;
import org.example.unisystem.service_interface.AssignmentService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/ui/assignments")
public class AssignmentUIController {

    private final AssignmentService assignmentService;

    public AssignmentUIController(AssignmentService assignmentService) {
        this.assignmentService = assignmentService;
    }

    @GetMapping
    public String listAssignments(@RequestParam(defaultValue = "0") int page,
                                  @RequestParam(defaultValue = "20") int size,
                                  Model model) {
        Pageable pageable = PageRequest.of(page, size);
        PaginationResponse<AssignmentDTO> response = assignmentService.getAllAssignments(pageable);
        model.addAttribute("pagination", response);
        return "assignments/list";
    }

    @GetMapping("/new")
    public String showForm(Model model) {
        model.addAttribute("assignment", new AssignmentCreateDTO());
        return "assignments/form";
    }

    @PostMapping
    public String saveAssignment(@ModelAttribute("assignment") @Valid AssignmentCreateDTO createDTO) {
        assignmentService.createAssignment(createDTO);
        return "redirect:/ui/assignments";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        AssignmentDTO assignment = assignmentService.getAssignmentById(id);
        model.addAttribute("assignment", assignment);
        return "assignments/edit";
    }

    @PostMapping("/{id}")
    public String updateAssignment(@PathVariable Long id,
                                   @ModelAttribute("assignment") @Valid AssignmentUpdateDTO updateDTO) {
        assignmentService.updateAssignment(id, updateDTO);
        return "redirect:/ui/assignments";
    }

    @GetMapping("/{id}/patch")
    public String showPatchForm(@PathVariable Long id, Model model) {
        AssignmentDTO assignment = assignmentService.getAssignmentById(id);
        model.addAttribute("assignment", assignment);
        return "assignments/patch";
    }

    @PostMapping("/{id}/patch")
    public String patchAssignment(@PathVariable Long id,
                                  @ModelAttribute("assignment") AssignmentPatchDTO patchDTO) {
        assignmentService.patchAssignment(id, patchDTO);
        return "redirect:/ui/assignments";
    }

    @PostMapping("/{id}/delete")
    public String deleteAssignment(@PathVariable Long id) {
        assignmentService.deleteAssignment(id);
        return "redirect:/ui/assignments";
    }
}
