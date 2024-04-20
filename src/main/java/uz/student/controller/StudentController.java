package uz.student.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.student.dto.request.StudentRequestDTO;
import uz.student.dto.response.StudentResponseDTO;
import uz.student.model.Student;
import uz.student.service.StudentService;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api")
public class StudentController {
private final  StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping("/student")
    public ResponseEntity<StudentResponseDTO> createStudentInfo(@RequestBody StudentRequestDTO studentRequestDTO){
        return ResponseEntity.ok(studentService.createStudentInfo(studentRequestDTO));
    }
    @DeleteMapping("student/{id}")
    public ResponseEntity deleteStudentById(@PathVariable Long id){
        return ResponseEntity.ok(studentService.deleteStudentById(id));
    }
    @GetMapping("/student/getAll")
    public ResponseEntity<?> getAll(){
        return ResponseEntity.ok(studentService.getAll());
    }
    @PutMapping("/student")
    public ResponseEntity updateStudentInfo(@RequestBody StudentRequestDTO studentRequestDTO ,
                                            @RequestParam Long id){
        return  ResponseEntity.ok(studentService.updateStudentInfo(studentRequestDTO,id));
    }
    @GetMapping("/student/{id}")
    public ResponseEntity getOneStudentById(@PathVariable Long id) {
        return ResponseEntity.ok(studentService.getOneStudentById(id));
    }
    @GetMapping("/student/export/pdf/{id}")
    public void exportStudentToPdf(@PathVariable Long id, HttpServletResponse response) throws IOException {
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=student.pdf");
        studentService.exportStudentToPdf(id, response.getOutputStream());
    }

    @GetMapping("/student/export/excel/{id}")
    public void exportStudentToExcel(@PathVariable Long id, HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment; filename=student.xlsx");
        studentService.exportStudentToExcel(id, response.getOutputStream());
    }
    @GetMapping("/students")
    public ResponseEntity<Page<StudentResponseDTO>> getFilteredStudents(
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            Pageable pageable) {

        Page<StudentResponseDTO> students = studentService.getFilteredStudents(firstName, lastName, pageable);
        return ResponseEntity.ok(students);
    }

    @GetMapping("/students/paging")
    public ResponseEntity getAllStudents(Pageable pageable){
        Page<Student> studentResponseDTO = studentService.getAllStudents(pageable);
        return ResponseEntity.ok(studentResponseDTO);
    }






    }




