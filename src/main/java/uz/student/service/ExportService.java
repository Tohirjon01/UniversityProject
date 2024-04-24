package uz.student.service;

import com.itextpdf.text.DocumentException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import uz.student.dto.response.StudentResponseDTO;

import java.io.IOException;
import java.io.OutputStream;

public interface ExportService {
    Page<StudentResponseDTO> getFilteredStudents(String firstName, String lastName, Pageable pageable);

    void exportStudentToExcel(OutputStream outputStream) throws IOException;

    byte[] exportStudentToPdf(Pageable pageable) throws IOException;

    void setStudentAvatar(Long studentId, MultipartFile multipartFile) throws IOException;

}
