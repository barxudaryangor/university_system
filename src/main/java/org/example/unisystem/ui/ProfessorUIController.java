package org.example.unisystem.ui;

import jakarta.validation.Valid;
import org.example.unisystem.dto.professor.ProfessorCreateDTO;
import org.example.unisystem.dto.professor.ProfessorDTO;
import org.example.unisystem.dto.professor.ProfessorPatchDTO;
import org.example.unisystem.dto.professor.ProfessorUpdateDTO;
import org.example.unisystem.dto.student.StudentDTO;
import org.example.unisystem.dto.student.StudentPatchDTO;
import org.example.unisystem.pagination.PaginationResponse;
import org.example.unisystem.service_interface.ProfessorService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import org.springframework.ui.Model;

@Controller
@RequestMapping("/ui/professors")
public class ProfessorUIController {
    private final ProfessorService professorService;

    public ProfessorUIController(ProfessorService professorService) {
        this.professorService = professorService;
    }

    @GetMapping
    public String listProfessors(@RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "20") int size,
                                 Model model) {
        Pageable pageable = PageRequest.of(page, size);
        PaginationResponse<ProfessorDTO> response = professorService.getAllProfessors(pageable);
        model.addAttribute("pagination", response);
        return "professors/list";
    }

    @GetMapping("/new")
    public String showForm(Model model) {
        model.addAttribute("professor", new ProfessorCreateDTO());
        return "professors/form";
    }

    @PostMapping
    public String saveProfessor(@ModelAttribute("professor") @Valid ProfessorCreateDTO createDTO) {
        professorService.createProfessor(createDTO);
        return "redirect:/ui/professors";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        ProfessorDTO professor = professorService.getProfessorById(id);
        model.addAttribute("professor", professor);
        return "professors/edit";
    }

    @PostMapping("/{id}")
    public String updateProfessor(@PathVariable Long id,
                                  @ModelAttribute("professor") @Valid ProfessorUpdateDTO updateDTO) {
        professorService.updateProfessor(id, updateDTO);
        return "redirect:/ui/professors";
    }

    @GetMapping("/{id}/patch")
    public String showPatchForm(@PathVariable Long id, Model model) {
        ProfessorDTO professor = professorService.getProfessorById(id);
        model.addAttribute("professor", professor);
        return "professors/patch";
    }

    @PostMapping("/{id}/patch")
    public String patchProfessor(@PathVariable Long id,
                                 @ModelAttribute("professor") ProfessorPatchDTO patchDTO) {
        professorService.patchProfessor(id, patchDTO);
        return "redirect:/ui/professors";
    }

    @PostMapping("/{id}/delete")
    public String deleteProfessor(@PathVariable Long id) {
        professorService.deleteProfessor(id);
        return "redirect:/ui/professors";
    }
}
