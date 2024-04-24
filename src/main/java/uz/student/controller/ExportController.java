package uz.student.controller;

import com.itextpdf.text.DocumentException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uz.student.dto.response.StudentResponseDTO;
import uz.student.service.ExportService;
import uz.student.service.ResumeCreator;
import uz.student.service.StudentService;

import java.io.IOException;
import java.io.OutputStream;

@RestController
@RequestMapping("/api")

public class ExportController {
    private final ExportService exportService;
    private final ResumeCreator resumeCreator;
    private final StudentService studentService;

    public ExportController(ExportService exportService, ResumeCreator resumeCreator, StudentService studentService) {
        this.exportService = exportService;
        this.resumeCreator = resumeCreator;
        this.studentService = studentService;
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
    @GetMapping("/export/pdf")
    public ResponseEntity<byte[]> exportStudentToPdf(Pageable pageable) throws IOException {

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Disposition",
                "attachment; filename=students.pdf");
        return new ResponseEntity<>(exportService.exportStudentToPdf(pageable), httpHeaders, HttpStatus.OK);

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
    @GetMapping("/export/createResume/{id}")
    public ResponseEntity<byte[]> createResumeToPdf(@PathVariable  Long id) throws DocumentException, IOException {

        StudentResponseDTO studentResponseDTO = studentService.getOneStudentById(id);
        String studentName = studentResponseDTO.getFirstName() + " " + studentResponseDTO.getLastName();


        byte[] pdfContent = resumeCreator.createResumeToPdf(id);
        String filename = studentName +  " Resume.pdf";
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_PDF);
        httpHeaders.setContentDispositionFormData("attachment", filename);

        return new ResponseEntity<>(pdfContent, httpHeaders, HttpStatus.OK);

    }


}

