package org.example.unisystem.ui;

import jakarta.validation.Valid;
import org.example.unisystem.dto.submission.SubmissionCreateDTO;
import org.example.unisystem.dto.submission.SubmissionDTO;
import org.example.unisystem.dto.submission.SubmissionPatchDTO;
import org.example.unisystem.dto.submission.SubmissionUpdateDTO;
import org.example.unisystem.pagination.PaginationResponse;
import org.example.unisystem.service_interface.SubmissionService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/ui/submissions")
public class SubmissionUIController {

    private final SubmissionService submissionService;

    public SubmissionUIController(SubmissionService submissionService) {
        this.submissionService = submissionService;
    }

    // --- LIST ---
    @GetMapping
    public String listSubmissions(@RequestParam(defaultValue = "0") int page,
                                  @RequestParam(defaultValue = "20") int size,
                                  Model model) {
        Pageable pageable = PageRequest.of(page, size);
        PaginationResponse<SubmissionDTO> response = submissionService.getAllSubmissions(pageable);
        model.addAttribute("pagination", response);
        return "submissions/list";
    }

    // --- CREATE ---
    @GetMapping("/new")
    public String showForm(Model model) {
        model.addAttribute("submission", new SubmissionCreateDTO());
        return "submissions/form";
    }

    @PostMapping
    public String saveSubmission(@ModelAttribute("submission") @Valid SubmissionCreateDTO createDTO) {
        submissionService.createSubmission(createDTO);
        return "redirect:/ui/submissions";
    }

    // --- EDIT ---
    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        SubmissionDTO submission = submissionService.getSubmissionById(id);
        model.addAttribute("submission", submission);
        return "submissions/edit";
    }

    @PostMapping("/{id}")
    public String updateSubmission(@PathVariable Long id,
                                   @ModelAttribute("submission") @Valid SubmissionUpdateDTO updateDTO) {
        submissionService.updateSubmission(id, updateDTO);
        return "redirect:/ui/submissions";
    }

    // --- PATCH ---
    @GetMapping("/{id}/patch")
    public String showPatchForm(@PathVariable Long id, Model model) {
        SubmissionDTO submission = submissionService.getSubmissionById(id);
        model.addAttribute("submission", submission);
        return "submissions/patch";
    }

    @PostMapping("/{id}/patch")
    public String patchSubmission(@PathVariable Long id,
                                  @ModelAttribute("submission") SubmissionPatchDTO patchDTO) {
        submissionService.patchSubmission(id, patchDTO);
        return "redirect:/ui/submissions";
    }

    // --- DELETE ---
    @PostMapping("/{id}/delete")
    public String deleteSubmission(@PathVariable Long id) {
        submissionService.deleteSubmission(id);
        return "redirect:/ui/submissions";
    }
}
