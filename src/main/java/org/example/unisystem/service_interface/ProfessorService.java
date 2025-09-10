package org.example.unisystem.service_interface;

import org.example.unisystem.dto.professor.ProfessorCreateDTO;
import org.example.unisystem.dto.professor.ProfessorDTO;
import org.example.unisystem.dto.professor.ProfessorPatchDTO;
import org.example.unisystem.dto.professor.ProfessorUpdateDTO;

import java.util.List;

public interface ProfessorService {
    ProfessorDTO getProfessorById(Long id);
    List<ProfessorDTO> getAllProfessors();
    ProfessorDTO createProfessor(ProfessorCreateDTO createDTO);
    ProfessorDTO updateProfessor(Long id, ProfessorUpdateDTO updateDTO);
    ProfessorDTO patchProfessor(Long id, ProfessorPatchDTO patchDTO);
    void deleteProfessor(Long id);

}
