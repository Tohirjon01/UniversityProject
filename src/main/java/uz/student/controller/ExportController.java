package uz.student.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uz.student.model.Student;
import uz.student.service.ExportService;
import uz.student.service.StudentService;

import java.io.IOException;
import java.io.OutputStream;

@RestController
@RequestMapping("/api")

public class ExportController {
    private final ExportService exportService;

    public ExportController(ExportService exportService) {
        this.exportService = exportService;
    }
    @GetMapping("/export/excel")
    public ResponseEntity<?> exportStudentsToExcel(HttpServletResponse response) {
        try {

            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=students.xlsx");
            OutputStream outputStream = response.getOutputStream();
            exportService.exportStudentToExcel(outputStream);
            outputStream.flush();
            outputStream.close();

            return ResponseEntity.ok().build();
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error exporting students to Excel");
        }
    }
    @GetMapping("/export/pdf/{id}")
    public ResponseEntity<?> exportStudentToPdf(@PathVariable Long id, HttpServletResponse response) {
        try {
            OutputStream outputStream = response.getOutputStream();
            exportService.exportStudentToPdf(id, outputStream);
            return ResponseEntity.ok().build();
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error exporting student to PDF");
        }
    }
    @PostMapping("/avatar/{studentId}")
    public ResponseEntity<?> setStudentAvatar(@PathVariable Long studentId, @RequestParam("file") MultipartFile file) {
        try {
            exportService.setStudentAvatar(studentId, file);
            return ResponseEntity.ok().body("Avatar set successfully");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error setting student avatar");
        }
    }
}

