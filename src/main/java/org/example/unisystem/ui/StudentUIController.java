package org.example.unisystem.ui;

import jakarta.validation.Valid;
import org.example.unisystem.dto.student.StudentCreateDTO;
import org.example.unisystem.dto.student.StudentDTO;
import org.example.unisystem.dto.student.StudentPatchDTO;
import org.example.unisystem.dto.student.StudentUpdateDTO;
import org.example.unisystem.pagination.PaginationResponse;
import org.example.unisystem.service_interface.StudentService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/ui/students")
public class StudentUIController {
    private final StudentService studentService;

    public StudentUIController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    public String listStudents(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            Model model) {
        Pageable pageable = PageRequest.of(page, size);
        PaginationResponse<StudentDTO> response = studentService.getAllStudents(pageable);
        model.addAttribute("pagination", response);
        return "students/list";
    }

    @GetMapping("/new")
    public String showForm(Model model) {
        model.addAttribute("student", new StudentCreateDTO());
        return "students/form";
    }

    @PostMapping
    public String saveStudent(@ModelAttribute("student") @Valid StudentCreateDTO studentDTO) {
        studentService.createStudent(studentDTO);
        return "redirect:/ui/students";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        StudentDTO student = studentService.getStudentById(id);
        model.addAttribute("student", student);
        return "students/edit";
    }

    @PostMapping("/{id}")
    public String updateStudent(@PathVariable Long id,
                                @ModelAttribute("student") @Valid StudentUpdateDTO studentUpdateDTO) {
        studentService.updateStudent(id, studentUpdateDTO);
        return "redirect:/ui/students";
    }

    @GetMapping("/{id}/patch")
    public String showPatchForm(@PathVariable Long id, Model model) {
        StudentDTO student = studentService.getStudentById(id);
        model.addAttribute("student", student);
        return "students/patch";
    }

    @PostMapping("/{id}/patch")
    public String patchStudent(@PathVariable Long id,
                               @ModelAttribute("student") StudentPatchDTO studentPatchDTO) {
        studentService.patchStudent(id, studentPatchDTO);
        return "redirect:/ui/students";
    }

    @PostMapping("/{id}/delete")
    public String deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return "redirect:/ui/students";
    }
}
