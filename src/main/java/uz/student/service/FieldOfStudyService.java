package uz.student.service;

import uz.student.dto.request.FieldRequestDTO;
import uz.student.dto.response.FieldResponseDTO;
import uz.student.dto.response.ResponseDTO;

import java.util.List;

public interface FieldOfStudyService {

    FieldResponseDTO createField(FieldRequestDTO fieldRequestDTO);
    FieldResponseDTO updateFieldInfo(FieldRequestDTO universityRequestDTO , Long id);
    ResponseDTO deleteFieldInfoById(Long id);
    List<FieldResponseDTO> getAllFieldInfo();

}
