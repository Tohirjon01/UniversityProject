package uz.student.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.student.dto.request.UniversityRequestDTO;
import uz.student.dto.response.ResponseDTO;
import uz.student.dto.response.UniversityResponseDTO;
import uz.student.model.University;
import uz.student.repository.UniversityRepository;
import uz.student.service.UniversityService;

import java.util.ArrayList;
import java.util.List;

@Service
public class UniversityServiceImpl implements UniversityService {

    private final UniversityRepository universityRepository;

    public UniversityServiceImpl(UniversityRepository universityRepository) {
        this.universityRepository = universityRepository;
    }


    @Override
    public UniversityResponseDTO createUniversity(UniversityRequestDTO universityRequestDTO) {
        University university = toEntity(universityRequestDTO);
        university = universityRepository.save(university);
        UniversityResponseDTO universityResponseDTO = toDto(university);

        return universityResponseDTO;
    }

    @Override
    public UniversityResponseDTO updateUniversityInfo(UniversityRequestDTO universityRequestDTO, Long id) {
        University university = universityRepository.findById(id).get();
        if (universityRequestDTO.getName() != null) {
            university.setName(universityRequestDTO.getName());
        }
        university = universityRepository.save(university);
        UniversityResponseDTO universityResponseDTO = toDto(university);
        return universityResponseDTO;
    }

    @Override
    public ResponseDTO deleteUniversityByID(Long id) {
        universityRepository.deleteById(id);
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setMessage("University:  " + id + " named " + "Deleted!");

        return responseDTO;
    }

    @Override
    public List<UniversityResponseDTO> getAll() {
        List<University> universityList = universityRepository.findAll();
        List<UniversityResponseDTO> universityResponseDTO = toDtos(universityList);
        return universityResponseDTO;
    }

    private University toEntity(UniversityRequestDTO universityRequestDTO) {
        University university = new University();
        university.setName(universityRequestDTO.getName());
        return university;
    }

    private UniversityResponseDTO toDto(University university) {
        UniversityResponseDTO universityResponseDTO = new UniversityResponseDTO();
        universityResponseDTO.setId(university.getId());
        universityResponseDTO.setName(university.getName());
        return universityResponseDTO;
    }

    private List<UniversityResponseDTO> toDtos(List<University> universityList) {
        List<UniversityResponseDTO> universityResponseDTOS = new ArrayList<>();
        for (int i = 0; i < universityList.size(); i++) {
            UniversityResponseDTO universityResponseDTO = new UniversityResponseDTO();
            universityResponseDTO.setName(universityList.get(i).getName());
            universityResponseDTO.setId(universityList.get(i).getId());
            universityResponseDTOS.add(universityResponseDTO);
        }
        return universityResponseDTOS;
    }

    @Transactional(readOnly = true)
    public Page<University> findAll(Pageable pageable){
        return universityRepository.findAll(pageable);
    }
}



