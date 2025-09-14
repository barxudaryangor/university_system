package org.example.unisystem.service;


import lombok.RequiredArgsConstructor;
import org.example.unisystem.dto.professor.ProfessorCreateDTO;
import org.example.unisystem.dto.professor.ProfessorDTO;
import org.example.unisystem.dto.professor.ProfessorPatchDTO;
import org.example.unisystem.dto.professor.ProfessorUpdateDTO;
import org.example.unisystem.entity.Professor;
import org.example.unisystem.exception.professor.ProfessorNotFoundException;
import org.example.unisystem.jpa_repo.ProfessorJpaRepository;
import org.example.unisystem.mappers.ProfessorMapper;
import org.example.unisystem.pagination.PaginationResponse;
import org.example.unisystem.patch.ProfessorPatchApplier;
import org.example.unisystem.service_interface.ProfessorService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProfessorServiceImpl implements ProfessorService {

    private final ProfessorJpaRepository professorJpaRepository;
    private final ProfessorMapper professorMapper;
    private final ProfessorPatchApplier patchApplier;

    @Override
    public ProfessorDTO getProfessorById(Long id) {
        Professor professor = professorJpaRepository.findByIdGraph(id)
                .orElseThrow(() -> new ProfessorNotFoundException(id));
        return professorMapper.professorToDTO(professor);
    }

    @Override
    public PaginationResponse<ProfessorDTO> getAllProfessors(Pageable pageable) {
        Page<Professor> page = professorJpaRepository.findAll(pageable);
        Page<ProfessorDTO> request = page.map(professorMapper::professorToDTO);
        return new PaginationResponse<>(request);
    }


    @Override
    @Transactional
    public ProfessorDTO createProfessor(ProfessorCreateDTO createDTO) {
        Professor professor = professorMapper.dtoToProfessor(createDTO);
        return professorMapper.professorToDTO(professorJpaRepository.save(professor));
    }

    @Override
    @Transactional
    public ProfessorDTO updateProfessor(Long id, ProfessorUpdateDTO updateDTO) {
        Professor professor = professorJpaRepository.findByIdGraph(id)
                .orElseThrow(() -> new ProfessorNotFoundException(id));
        professorMapper.updateProfessorFromDTO(updateDTO, professor);
        return professorMapper.professorToDTO(professor);
    }

    @Override
    @Transactional
    public ProfessorDTO patchProfessor(Long id, ProfessorPatchDTO patchDTO) {
        Professor professor = professorJpaRepository.findByIdGraph(id)
                .orElseThrow(() -> new ProfessorNotFoundException(id));
        patchApplier.patchProfessor(professor, patchDTO);
        return professorMapper.professorToDTO(professorJpaRepository.save(professor));
    }

    @Override
    @Transactional
    public void deleteProfessor(Long id) {
        Professor professor = professorJpaRepository.findByIdGraph(id)
                .orElseThrow(() -> new ProfessorNotFoundException(id));
        professorJpaRepository.delete(professor);
    }
}
