package uz.student.service;

import org.springframework.web.multipart.MultipartFile;
import uz.student.dto.request.FieldRequestDTO;
import uz.student.dto.response.FieldResponseDTO;
import uz.student.dto.response.ResponseDTO;
import uz.student.model.FileStorage;

import java.util.List;

public interface FieldOfStudyService {

    FieldResponseDTO createField(FieldRequestDTO fieldRequestDTO);
    FieldResponseDTO updateFieldInfo(FieldRequestDTO universityRequestDTO , Long id);
    ResponseDTO deleteFieldInfoById(Long id);
    List<FieldResponseDTO> getAllFieldInfo();

    FileStorage save(MultipartFile multipartFile);
}
