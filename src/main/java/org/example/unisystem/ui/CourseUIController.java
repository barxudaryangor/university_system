package org.example.unisystem.ui;
import jakarta.validation.Valid;
import org.example.unisystem.dto.course.CourseCreateDTO;
import org.example.unisystem.dto.course.CourseDTO;
import org.example.unisystem.dto.course.CoursePatchDTO;
import org.example.unisystem.dto.course.CourseUpdateDTO;
import org.example.unisystem.pagination.PaginationResponse;
import org.example.unisystem.service_interface.CourseService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import org.springframework.ui.Model;
@Controller
@RequestMapping("/ui/courses")
public class CourseUIController {
    private final CourseService courseService;

    public CourseUIController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping
    public String listCourses(@RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "20") int size,
                              Model model) {
        Pageable pageable = PageRequest.of(page, size);
        PaginationResponse<CourseDTO> response = courseService.getAllCourses(pageable);
        model.addAttribute("pagination", response);
        return "courses/list";
    }

    @GetMapping("/new")
    public String showForm(Model model) {
        model.addAttribute("course", new CourseCreateDTO());
        return "courses/form";
    }

    @PostMapping
    public String saveCourse(@ModelAttribute("course") @Valid CourseCreateDTO createDTO) {
        courseService.createCourse(createDTO);
        return "redirect:/ui/courses";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        CourseDTO course = courseService.getCourseById(id);
        model.addAttribute("course", course);
        return "courses/edit";
    }

    @PostMapping("/{id}")
    public String updateCourse(@PathVariable Long id,
                               @ModelAttribute("course") @Valid CourseUpdateDTO updateDTO) {
        courseService.updateCourse(id, updateDTO);
        return "redirect:/ui/courses";
    }

    @GetMapping("/{id}/patch")
    public String showPatchForm(@PathVariable Long id, Model model) {
        CourseDTO course = courseService.getCourseById(id);
        model.addAttribute("course", course);
        return "courses/patch";
    }

    @PostMapping("/{id}/patch")
    public String patchCourse(@PathVariable Long id,
                              @ModelAttribute("course") CoursePatchDTO patchDTO) {
        courseService.patchCourse(id, patchDTO);
        return "redirect:/ui/courses";
    }

    @PostMapping("/{id}/delete")
    public String deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
        return "redirect:/ui/courses";
    }
}