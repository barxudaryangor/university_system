package org.example.unisystem.mapper_test;

import jakarta.transaction.Transactional;
import org.example.unisystem.dto.professor.ProfessorCreateDTO;
import org.example.unisystem.dto.professor.ProfessorDTO;
import org.example.unisystem.entity.Course;
import org.example.unisystem.entity.Professor;
import org.example.unisystem.jpa_repo.CourseJpaRepository;
import org.example.unisystem.mappers.ProfessorMapper;
import org.example.unisystem.short_dto.CourseShortDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
public class ProfessorMapperTest {
    @Autowired
    ProfessorMapper professorMapper;

    @Autowired
    CourseJpaRepository courseJpaRepository;



    @Test
    void professorToDTO() {
        Course c = new Course(1L, "title", 10, null, null, null);

        Professor p = new Professor();
        p.setId(2L);
        p.setName("name");
        p.setDepartment("department");
        p.setCourses(Set.of(c));

        ProfessorDTO dto = professorMapper.professorToDTO(p);

        assertEquals(2L, dto.getId());
        assertEquals("name", dto.getName());
        assertEquals("department", dto.getDepartment());
        assertEquals(1L, dto.getCourses().get(0).id());
    }

    @Test
    void dtoToProfessor() {
        Course c = new Course(null, "title", 10, null, null, null);
        courseJpaRepository.save(c);

        CourseShortDTO shortDTO = new CourseShortDTO(
                c.getId(), c.getTitle(), c.getCredits()
        );

        ProfessorCreateDTO dto = new ProfessorCreateDTO("name", "surname", "department", List.of(shortDTO));

        Professor professor = professorMapper.dtoToProfessor(dto);
        assertEquals(c.getId(), professor.getCourses().iterator().next().getId());
        assertEquals("name", professor.getName());
        assertEquals("surname", professor.getSurname());
        assertEquals("department", professor.getDepartment());
    }
}
