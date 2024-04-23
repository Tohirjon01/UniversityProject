package uz.student.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uz.student.dto.request.StudentRequestDTO;
import uz.student.dto.response.StudentResponseDTO;
import uz.student.model.Student;
import uz.student.service.StudentService;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class StudentController {
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }


    @PostMapping("/student")
    public ResponseEntity<StudentResponseDTO> createStudentInfo(@RequestBody StudentRequestDTO studentRequestDTO) {
        return ResponseEntity.ok(studentService.createStudentInfo(studentRequestDTO));
    }

    @DeleteMapping("student/{id}")
    public ResponseEntity deleteStudentById(@PathVariable Long id) {
        return ResponseEntity.ok(studentService.deleteStudentById(id));
    }

    @GetMapping("/student/getAll")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(studentService.getAll());
    }

    @PutMapping("/student")
    public ResponseEntity updateStudentInfo(@RequestBody StudentRequestDTO studentRequestDTO,
                                            @RequestParam Long id) {
        return ResponseEntity.ok(studentService.updateStudentInfo(studentRequestDTO, id));
    }

    @GetMapping("/student/{id}")
    public ResponseEntity getOneStudentById(@PathVariable Long id) {
        return ResponseEntity.ok(studentService.getOneStudentById(id));
    }
}












