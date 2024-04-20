package uz.student.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.student.dto.request.UniversityRequestDTO;
import uz.student.dto.response.UniversityResponseDTO;
import uz.student.service.UniversityService;


@RestController
@RequestMapping("/api")
public class UniversityController {

    private final UniversityService universityService;

    public UniversityController(UniversityService universityService) {
        this.universityService = universityService;
    }

    @PostMapping("/university")
    public ResponseEntity<UniversityResponseDTO> createUniversity(
            @RequestBody UniversityRequestDTO universityRequestDTO) {
        return ResponseEntity.ok(universityService.createUniversity(universityRequestDTO));
    }


    @PutMapping("/university")
    public ResponseEntity updateUniversityInfo(@RequestBody UniversityRequestDTO universityRequestDTO,
                                               @RequestParam Long id) {
        return ResponseEntity.ok(universityService.updateUniversityInfo(universityRequestDTO, id));
    }

    @DeleteMapping("/university/{id}")
    public ResponseEntity deleteUniversityByID(@PathVariable Long id) {
        return ResponseEntity.ok(universityService.deleteUniversityByID(id));
    }

    @GetMapping("/university/getAll")
    public ResponseEntity<?> getAllUniversities(){
        return ResponseEntity.ok(universityService.getAll());
    }
}
