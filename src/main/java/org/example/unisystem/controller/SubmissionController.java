package org.example.unisystem.controller;

import org.example.unisystem.dto.submission.SubmissionCreateDTO;
import org.example.unisystem.dto.submission.SubmissionDTO;
import org.example.unisystem.dto.submission.SubmissionPatchDTO;
import org.example.unisystem.dto.submission.SubmissionUpdateDTO;
import org.example.unisystem.pagination.PaginationResponse;
import org.example.unisystem.service_interface.SubmissionService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/uni/submissions")
public class SubmissionController {
    private final SubmissionService submissionService;

    public SubmissionController(SubmissionService submissionService) {
        this.submissionService = submissionService;
    }

    @GetMapping("/{id}")
    SubmissionDTO getSubmissionById(@PathVariable Long id) {
        return submissionService.getSubmissionById(id);
    }

    @GetMapping
    PaginationResponse<SubmissionDTO> getAllSubmissions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size

    ) {
        Pageable pageable = PageRequest.of(page,size);
        return submissionService.getAllSubmissions(pageable);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    SubmissionDTO createSubmission(@Validated @RequestBody SubmissionCreateDTO createDTO) {
        return submissionService.createSubmission(createDTO);
    }

    @PutMapping("/{id}")
    SubmissionDTO updateSubmission(@PathVariable Long id, @Validated @RequestBody SubmissionUpdateDTO updateDTO) {
        return submissionService.updateSubmission(id, updateDTO);
    }

    @PatchMapping("/{id}")
    SubmissionDTO patchSubmission(@PathVariable Long id, @RequestBody SubmissionPatchDTO patchDTO) {
        return submissionService.patchSubmission(id, patchDTO);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteSubmission(@PathVariable Long id) {
        submissionService.deleteSubmission(id);
        return ResponseEntity.noContent().build();
    }
}
