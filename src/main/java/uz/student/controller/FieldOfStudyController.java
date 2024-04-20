package uz.student.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.student.dto.request.FieldRequestDTO;
import uz.student.dto.response.FieldResponseDTO;
import uz.student.service.FieldOfStudyService;

@RestController
@RequestMapping("/api")
public class FieldOfStudyController {
    private final FieldOfStudyService fieldOfStudyService;

    public FieldOfStudyController(FieldOfStudyService fieldOfStudyService) {
        this.fieldOfStudyService = fieldOfStudyService;
    }


    @PostMapping("/field")
    public ResponseEntity<FieldResponseDTO> createField(
            @RequestBody FieldRequestDTO fieldRequestDTO ){

        return ResponseEntity.ok(fieldOfStudyService.createField(fieldRequestDTO));
    }

    @PutMapping("/field")
    public ResponseEntity updateFieldInfo(@RequestBody FieldRequestDTO fieldRequestDTO ,
                                          @RequestParam Long id){
        return ResponseEntity.ok(fieldOfStudyService.updateFieldInfo(fieldRequestDTO,id));
    }
    @DeleteMapping("/field/{id}")
    public ResponseEntity deleteFieldInfoById (@PathVariable Long id){
        return ResponseEntity.ok(fieldOfStudyService.deleteFieldInfoById(id));
    }

    @GetMapping("/field/getAll")
    public ResponseEntity<?> getAllFieldInfo(){
        return ResponseEntity.ok(fieldOfStudyService.getAllFieldInfo());
    }
}
