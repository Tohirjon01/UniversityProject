package uz.student.service;

import uz.student.dto.request.UniversityRequestDTO;
import uz.student.dto.response.ResponseDTO;
import uz.student.dto.response.UniversityResponseDTO;

import java.util.List;
import java.util.Objects;

public interface UniversityService {
    UniversityResponseDTO createUniversity(UniversityRequestDTO universityRequestDTO);
    UniversityResponseDTO updateUniversityInfo(UniversityRequestDTO  universityRequestDTO , Long id);
    ResponseDTO deleteUniversityByID(Long id);
    List<UniversityResponseDTO> getAll();

}