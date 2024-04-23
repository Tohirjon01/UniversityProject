package uz.student.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uz.student.dto.request.FieldRequestDTO;
import uz.student.dto.response.FieldResponseDTO;
import uz.student.dto.response.ResponseDTO;
import uz.student.model.FieldOfStudy;
import uz.student.model.FileStorage;
import uz.student.model.University;
import uz.student.repository.FieldOfStudyRepository;
import uz.student.repository.UniversityRepository;
import uz.student.service.FieldOfStudyService;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class FieldServiceImpl implements FieldOfStudyService {
    //
    private final FieldOfStudyRepository fieldOfStudyRepository;
    private final UniversityRepository universityRepository;

    @Override
    public FieldResponseDTO createField(FieldRequestDTO fieldRequestDTO) {
        FieldOfStudy fieldOfStudy = toEntity(fieldRequestDTO);
        fieldOfStudy = fieldOfStudyRepository.save(fieldOfStudy);
        University university = universityRepository.findById(fieldOfStudy.getUniversity().getId()).get();
        FieldResponseDTO fieldResponseDTO = toDto(fieldOfStudy);
        fieldResponseDTO.setUniversityName(university.getName());

        return fieldResponseDTO;
    }

    @Override
    public FieldResponseDTO updateFieldInfo(FieldRequestDTO fieldRequestDTO, Long id) {
        FieldOfStudy fieldOfStudy = fieldOfStudyRepository.findById(id).get();
        University university = universityRepository.findById(fieldOfStudy.getUniversity().getId()).get();
        updateFromDto(fieldRequestDTO, fieldOfStudy);
        fieldOfStudy = fieldOfStudyRepository.save(fieldOfStudy);
        FieldResponseDTO fieldResponseDTO = toDto(fieldOfStudy);
        fieldResponseDTO.setUniversityName(university.getName());
        return fieldResponseDTO;
    }

    @Override
    public ResponseDTO deleteFieldInfoById(Long id) {
        fieldOfStudyRepository.deleteById(id);
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setMessage("Field: " + id + " named " + "Deleted!");
        return responseDTO;
    }

    @Override
    public List<FieldResponseDTO> getAllFieldInfo() {
        List<FieldOfStudy> fieldOfStudiesList = fieldOfStudyRepository.findAll();
        List<FieldResponseDTO> universityResponseDTOS = toDtos(fieldOfStudiesList);
        return universityResponseDTOS;
    }

    @Override
    public FileStorage save(MultipartFile multipartFile) {
        return null;
    }

    private FieldOfStudy toEntity(FieldRequestDTO fieldRequestDTO) {
        FieldOfStudy fieldOfStudy = new FieldOfStudy();
        if (fieldRequestDTO.getName().isBlank()){
            throw  new RuntimeException("Field name must be filled ");
        }
        fieldOfStudy.setName(fieldRequestDTO.getName());



        var universityOptional = universityRepository.findById(fieldRequestDTO.getUniversityId());
        if (universityOptional.isEmpty()){
            throw new RuntimeException("Not found University by universityId : " +  fieldOfStudy.getUniversity());
        }

        var university = universityOptional.get();
        fieldOfStudy.setUniversity(university);
        return fieldOfStudy;
    }

    private FieldResponseDTO toDto(FieldOfStudy fieldOfStudy) {
        FieldResponseDTO fieldResponseDTO = new FieldResponseDTO();
        fieldResponseDTO.setName(fieldOfStudy.getName());
        fieldResponseDTO.setId(fieldOfStudy.getId());
        return fieldResponseDTO;
    }

    private void updateFromDto(FieldRequestDTO fieldRequestDTO, FieldOfStudy fieldOfStudy) {

        if (fieldRequestDTO.getName() != null) {
            fieldOfStudy.setName(fieldRequestDTO.getName());
        }
        if (fieldRequestDTO.getUniversityId() != null){
            fieldOfStudy.getUniversity().setId(fieldRequestDTO.getUniversityId());
        }


    }
    private List<FieldResponseDTO> toDtos(List<FieldOfStudy> fieldOfStudyList){
        List<FieldResponseDTO> fieldResponseDTOS = new ArrayList<>();
        for (int i = 0; i < fieldOfStudyList.size(); i++) {
            FieldResponseDTO fieldResponseDTO = new FieldResponseDTO();
            University university = universityRepository.findById(fieldOfStudyList.get(i).getUniversity().getId()).get();
            fieldResponseDTO.setName((fieldOfStudyList.get(i).getName()));
            fieldResponseDTO.setUniversityName(university.getName());
            fieldResponseDTO.setId(fieldOfStudyList.get(i).getId());
            fieldResponseDTOS.add(fieldResponseDTO);

        }
        return fieldResponseDTOS;

    }
}