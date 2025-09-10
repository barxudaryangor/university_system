package org.example.unisystem.service_test;

import org.example.unisystem.dto.assignment.AssignmentCreateDTO;
import org.example.unisystem.dto.assignment.AssignmentDTO;
import org.example.unisystem.dto.assignment.AssignmentPatchDTO;
import org.example.unisystem.dto.assignment.AssignmentUpdateDTO;
import org.example.unisystem.dto.student.StudentUpdateDTO;
import org.example.unisystem.entity.Assignment;
import org.example.unisystem.entity.Student;
import org.example.unisystem.jpa_repo.AssignmentJpaRepository;
import org.example.unisystem.mappers.AssignmentMapper;
import org.example.unisystem.patch.AssignmentPatchApplier;
import org.example.unisystem.service.AssignmentServiceImpl;
import org.example.unisystem.service_interface.AssignmentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class AssignmentServiceTest {

    @Mock
    AssignmentJpaRepository assignmentJpaRepository;

    @Mock
    AssignmentMapper assignmentMapper;

    @Mock
    AssignmentPatchApplier assignmentPatchApplier;

    @InjectMocks
    AssignmentServiceImpl assignmentService;

    @Test
    void getAssignmentById() {
        Assignment assignment = new Assignment(
                1L, "Title", LocalDate.of(2026,11,11),
                null, null
        );

        when(assignmentJpaRepository.findByIdGraph(1L)).thenReturn(Optional.of(assignment));

        when(assignmentMapper.assignmentToDTO(assignment)).thenReturn(
                new AssignmentDTO(
                        assignment.getId(),
                        assignment.getTitle(),
                        assignment.getDueDate(),
                        null,
                        null
                )
        );

        AssignmentDTO dto = assignmentService.getAssignmentById(1L);
        assertEquals(assignment.getId(), dto.getId());
        assertEquals(assignment.getDueDate(), dto.getDueDate());
        assertEquals(assignment.getTitle(), dto.getTitle());

    }

    @Test
    void getAllAssignments() {
        Assignment assignment = new Assignment(
                1L, "Title", LocalDate.of(2026,11,11),
                null, null
        );

        Assignment assignment2 = new Assignment(
                2L, "Title2", LocalDate.of(2027,12,12),
                null, null
        );

        when(assignmentJpaRepository.findAll()).thenReturn(List.of(assignment,assignment2));

        when(assignmentMapper.assignmentToDTO(assignment)).thenReturn(
                new AssignmentDTO(
                        assignment.getId(),assignment.getTitle(),assignment.getDueDate(),
                        null, null
                )
        );

        when(assignmentMapper.assignmentToDTO(assignment2)).thenReturn(
                new AssignmentDTO(
                        assignment2.getId(), assignment2.getTitle(), assignment2.getDueDate(),
                        null, null
                )
        );

        List<AssignmentDTO> assignments = assignmentService.getAllAssignments();

        assertEquals(assignment.getId(), assignments.get(0).getId());
        assertEquals(assignment.getDueDate(), assignments.get(0).getDueDate());
        assertEquals(assignment.getTitle(), assignments.get(0).getTitle());

        assertEquals(assignment2.getId(), assignments.get(1).getId());
        assertEquals(assignment2.getDueDate(), assignments.get(1).getDueDate());
        assertEquals(assignment2.getTitle(), assignments.get(1).getTitle());
    }

    @Test
    void createAssignment() {

        AssignmentCreateDTO request = new AssignmentCreateDTO(
                "Title", LocalDate.of(2027,12,12),
                null, null
        );

        Assignment entity = new Assignment(
                null, "Title", LocalDate.of(2027,12,12),
                null, null
        );

        Assignment savedEntity = new Assignment(
                1L, "Title", LocalDate.of(2027,12,12),
                null, null
        );

        AssignmentDTO response = new AssignmentDTO(
                1L, "Title", LocalDate.of(2027,12,12),
                null, null
        );


        when(assignmentMapper.dtoToAssignment(request)).thenReturn(entity);
        when(assignmentJpaRepository.save(entity)).thenReturn(savedEntity);
        when(assignmentMapper.assignmentToDTO(savedEntity)).thenReturn(response);

        AssignmentDTO dto = assignmentService.createAssignment(request);

        assertEquals(response.getId(), dto.getId());
        assertEquals(response.getTitle(), dto.getTitle());
        assertEquals(response.getDueDate(), dto.getDueDate());
    }

    @Test
    void updateAssignment() {
        Assignment assignment = new Assignment(
                1L, "Title", LocalDate.of(2027,11,11),
                null, null
        );

        AssignmentUpdateDTO assignmentUpdate = new AssignmentUpdateDTO(
                "Title2", LocalDate.of(2028,12,12),
                null, null
        );

        Assignment updatedAssignment = new Assignment(
                1L, "Title2", LocalDate.of(2028,12,12),
                null, null
        );

        AssignmentDTO dto2 = new AssignmentDTO(
                1L, "Title2", LocalDate.of(2028,12,12),
                null, null
        );

        doAnswer(invocation -> {
            AssignmentUpdateDTO dto = invocation.getArgument(0);
            Assignment assignment1 = invocation.getArgument(1);
            assignment1.setTitle(dto.getTitle());
            assignment1.setDueDate(dto.getDueDate());
            return null;
        }).when(assignmentMapper).updateAssignmentFromDTO(any(AssignmentUpdateDTO.class), any(Assignment.class));

        when(assignmentJpaRepository.findByIdGraph(1L)).thenReturn(Optional.of(assignment));
        when(assignmentJpaRepository.save(assignment)).thenReturn(updatedAssignment);
        when(assignmentMapper.assignmentToDTO(updatedAssignment)).thenReturn(dto2);

        AssignmentDTO assignmentDTO = assignmentService.updateAssignment(1L, assignmentUpdate);

        assertEquals(dto2.getDueDate(), assignmentDTO.getDueDate());
        assertEquals(dto2.getTitle(), assignmentDTO.getTitle());

        verify(assignmentJpaRepository).save(assignment);
    }

    @Test
    void patchAssignment() {
        Assignment assignment = new Assignment(
                1L, "Title", LocalDate.of(2027,11,11),
                null, null
        );

        when(assignmentJpaRepository.findByIdGraph(1L)).thenReturn(Optional.of(assignment));

        doAnswer(invocation -> {
            Assignment a = invocation.getArgument(0);
            AssignmentPatchDTO dto = invocation.getArgument(1);
            a.setDueDate(dto.getDueDate());
            a.setTitle(dto.getTitle());
            return null;
        }).when(assignmentPatchApplier).patchAssignment(any(Assignment.class), any(AssignmentPatchDTO.class));

        AssignmentPatchDTO assignmentPatchDTO = new AssignmentPatchDTO(
                "Title2", LocalDate.of(2028,11,11),
                null,null
        );

        Assignment newAssignment = new Assignment(
                1L,"Title2", LocalDate.of(2028,11,11),
                null,null
        );

        AssignmentDTO newDTO = new AssignmentDTO(
                1L,"Title2", LocalDate.of(2028,11,11),
                null,null
        );

        when(assignmentJpaRepository.save(assignment)).thenReturn(newAssignment);
        when(assignmentMapper.assignmentToDTO(newAssignment)).thenReturn(newDTO);

        AssignmentDTO assDTO = assignmentService.patchAssignment(1L, assignmentPatchDTO);

        assertEquals(newDTO.getId(), assDTO.getId());
        assertEquals(newDTO.getTitle(), assDTO.getTitle());
        assertEquals(newDTO.getDueDate(), assDTO.getDueDate());

        verify(assignmentPatchApplier).patchAssignment(eq(assignment), eq(assignmentPatchDTO));
    }

    @Test
    void deleteAssignment() {
        Assignment assignment = new Assignment(
                1L, "Title", LocalDate.of(2027,11,11),
                null, null
        );

        when(assignmentJpaRepository.findByIdGraph(1L)).thenReturn(Optional.of(assignment));

        assignmentService.deleteAssignment(1L);

        verify(assignmentJpaRepository).delete(assignment);
        verify(assignmentJpaRepository).findByIdGraph(1L);
        verifyNoMoreInteractions(assignmentJpaRepository);


    }
}
