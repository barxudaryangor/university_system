package org.example.unisystem.controller;

import org.example.unisystem.dto.professor.ProfessorCreateDTO;
import org.example.unisystem.dto.professor.ProfessorDTO;
import org.example.unisystem.dto.professor.ProfessorPatchDTO;
import org.example.unisystem.dto.professor.ProfessorUpdateDTO;
import org.example.unisystem.service_interface.ProfessorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/uni/professors")
public class ProfessorController {
    private final ProfessorService professorService;

    public ProfessorController(ProfessorService professorService) {
        this.professorService = professorService;
    }

    @GetMapping("/{id}")
    ProfessorDTO getProfessorById(@PathVariable Long id) {
        return professorService.getProfessorById(id);
    }

    @GetMapping
    List<ProfessorDTO> getAllProfessors() {
        return professorService.getAllProfessors();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    ProfessorDTO createProfessor(@Validated @RequestBody ProfessorCreateDTO createDTO) {
        return professorService.createProfessor(createDTO);
    }

    @PutMapping("/{id}")
    ProfessorDTO updateProfessor(@PathVariable Long id, @Validated @RequestBody ProfessorUpdateDTO updateDTO) {
        return professorService.updateProfessor(id, updateDTO);
    }

    @PatchMapping("/{id}")
    ProfessorDTO patchProfessor(@PathVariable Long id, @Validated @RequestBody ProfessorPatchDTO patchDTO) {
        return professorService.patchProfessor(id, patchDTO);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteProfessor(@PathVariable Long id) {
        professorService.deleteProfessor(id);
        return ResponseEntity.noContent().build();
    }
}
