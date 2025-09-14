package org.example.unisystem.service_test;

import org.example.unisystem.dto.professor.ProfessorCreateDTO;
import org.example.unisystem.dto.professor.ProfessorDTO;
import org.example.unisystem.dto.professor.ProfessorPatchDTO;
import org.example.unisystem.dto.professor.ProfessorUpdateDTO;
import org.example.unisystem.entity.Professor;
import org.example.unisystem.jpa_repo.ProfessorJpaRepository;
import org.example.unisystem.mappers.ProfessorMapper;
import org.example.unisystem.pagination.PaginationResponse;
import org.example.unisystem.patch.ProfessorPatchApplier;
import org.example.unisystem.service.ProfessorServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProfessorServiceTest {
    @Mock
    ProfessorMapper professorMapper;

    @Mock
    ProfessorJpaRepository professorJpaRepository;

    @Mock
    ProfessorPatchApplier patchApplier;

    @InjectMocks
    ProfessorServiceImpl professorService;

    @Test
    void getProfessorById() {
        Professor professor = new Professor(
                1L, "Name", "Surname", "Department", null
        );

        when(professorJpaRepository.findByIdGraph(1L)).thenReturn(Optional.of(professor));

        when(professorMapper.professorToDTO(professor)).thenReturn(
                new ProfessorDTO(
                        professor.getId(),
                        professor.getName(),
                        professor.getSurname(),
                        professor.getDepartment(),
                        null)
        );

        ProfessorDTO professorDTO = professorService.getProfessorById(1L);

        assertEquals(professor.getId(), professorDTO.getId());
        assertEquals(professor.getName(), professorDTO.getName());
        assertEquals(professor.getSurname(), professorDTO.getSurname());
        assertEquals(professor.getDepartment(), professorDTO.getDepartment());

        verify(professorJpaRepository).findByIdGraph(1L);

    }

    @Test
    void getAllProfessors() {
        Professor professor = new Professor(
                1L, "Name", "Surname", "Department", null
        );

        when(professorMapper.professorToDTO(professor)).thenReturn(
                new ProfessorDTO(
                        professor.getId(),
                        professor.getName(),
                        professor.getSurname(),
                        professor.getDepartment(),
                        null)
        );

        Professor professor2 = new Professor(
                2L, "Name2", "Surname2", "Department2", null
        );

        when(professorMapper.professorToDTO(professor2)).thenReturn(
                new ProfessorDTO(
                        professor2.getId(),
                        professor2.getName(),
                        professor2.getSurname(),
                        professor2.getDepartment(),
                        null)
        );

        Page<Professor> page = new PageImpl<>(List.of(professor,professor2), PageRequest.of(0,10), 2);
        when(professorJpaRepository.findAll(any(Pageable.class))).thenReturn(page);

        PaginationResponse<ProfessorDTO> response = professorService.getAllProfessors(PageRequest.of(0,10));
        List<ProfessorDTO> professors = response.getContent();

        assertEquals(professors.get(0).getId(), professor.getId());
        assertEquals(professors.get(0).getName(), professor.getName());
        assertEquals(professors.get(0).getSurname(), professor.getSurname());
        assertEquals(professors.get(0).getDepartment(), professor.getDepartment());

        assertEquals(professors.get(1).getId(), professor2.getId());
        assertEquals(professors.get(1).getName(), professor2.getName());
        assertEquals(professors.get(1).getSurname(), professor2.getSurname());
        assertEquals(professors.get(1).getDepartment(), professor2.getDepartment());

        assertEquals(0, response.getPageNum());
        assertEquals(10, response.getPageSize());
        assertEquals(2, response.getTotalElements());
        assertEquals(1, response.getTotalPages());
        assertEquals(true, response.isLast());

        verify(professorJpaRepository).findAll(any(Pageable.class));
    }

    @Test
    void createProfessor() {
        ProfessorCreateDTO createDTO = new ProfessorCreateDTO(
                "Name", "Surname", "Department", null
        );

        Professor professor = new Professor(
                null, "Name", "Surname", "Department", null
        );

        Professor savedProfessor = new Professor(
                1L, "Name", "Surname", "Department", null
        );

        ProfessorDTO response = new ProfessorDTO(
                1L, "Name", "Surname", "Department", null
        );

        when(professorMapper.dtoToProfessor(createDTO)).thenReturn(professor);
        when(professorJpaRepository.save(professor)).thenReturn(savedProfessor);
        when(professorMapper.professorToDTO(savedProfessor)).thenReturn(response);

        ProfessorDTO dto = professorService.createProfessor(createDTO);

        assertEquals(dto.getId(), 1L);
        assertEquals(createDTO.getName(), dto.getName());
        assertEquals(createDTO.getSurname(), dto.getSurname());
        assertEquals(createDTO.getDepartment(), dto.getDepartment());

    }

    @Test
    void updateProfessor() {
        ProfessorUpdateDTO updateDTO = new ProfessorUpdateDTO(
                "Name", "Surname", "Department", null
        );

        Professor professor = new Professor(
                1L, "Name2", "Surname2", "Depatment2", null
        );

        ProfessorDTO mappedDTO = new ProfessorDTO(
                1L, "Name", "Surname", "Department", null
        );

        when(professorJpaRepository.findByIdGraph(1L)).thenReturn(Optional.of(professor));

        doAnswer(invocation -> {
            ProfessorUpdateDTO dto = invocation.getArgument(0);
            Professor p = invocation.getArgument(1);
            p.setName(dto.getName());
            p.setSurname(dto.getSurname());
            p.setDepartment(dto.getDepartment());
            return null;
        }).when(professorMapper).updateProfessorFromDTO(any(ProfessorUpdateDTO.class), any(Professor.class));

        when(professorMapper.professorToDTO(professor)).thenReturn(mappedDTO);

        ProfessorDTO response = professorService.updateProfessor(1L, updateDTO);

        assertEquals(response.getId(), 1L);
        assertEquals(updateDTO.getName(), response.getName());
        assertEquals(updateDTO.getSurname(), response.getSurname());
        assertEquals(updateDTO.getDepartment(), response.getDepartment());


    }

    @Test
    void patchProfessor() {
        Professor professor = new Professor(
                1L, "Name", "Surname", "Department", null
        );

        when(professorJpaRepository.findByIdGraph(1L)).thenReturn(Optional.of(professor));

        doAnswer(invocation -> {
            Professor p = invocation.getArgument(0);
            ProfessorPatchDTO pDTO = invocation.getArgument(1);
            p.setName(pDTO.getName());
            p.setSurname(pDTO.getSurname());
            p.setDepartment(pDTO.getDepartment());
            return null;
        }).when(patchApplier).patchProfessor(any(Professor.class), any(ProfessorPatchDTO.class));

        ProfessorPatchDTO patchDTO = new ProfessorPatchDTO(
                "Name2", "Surname2", "Department2", null
        );

        Professor newProfessor = new Professor(
                1L, "Name2", "Surname2", "Department2", null
        );

        ProfessorDTO newDTO = new ProfessorDTO(
                1L, "Name2", "Surname2", "Department2", null
        );

        when(professorJpaRepository.save(professor)).thenReturn(newProfessor);
        when(professorMapper.professorToDTO(newProfessor)).thenReturn(newDTO);

        ProfessorDTO response = professorService.patchProfessor(1L, patchDTO);

        assertEquals(response.getId(), 1L);
        assertEquals(patchDTO.getName(), response.getName());
        assertEquals(patchDTO.getSurname(), response.getSurname());
        assertEquals(patchDTO.getDepartment(), response.getDepartment());

        verify(patchApplier).patchProfessor(eq(professor), eq(patchDTO));
    }

    @Test
    void deleteProfessor() {
        Professor professor = new Professor(
                1L, "Name", "Surname", "Department", null
        );

        when(professorJpaRepository.findByIdGraph(1L)).thenReturn(Optional.of(professor));

        professorService.deleteProfessor(1L);

        verify(professorJpaRepository).delete(professor);
        verify(professorJpaRepository).findByIdGraph(1L);
        verifyNoMoreInteractions(professorJpaRepository);
    }
}
