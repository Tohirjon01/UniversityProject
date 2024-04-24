package uz.student.service;

import com.itextpdf.text.DocumentException;
import org.springframework.data.domain.Pageable;
import uz.student.dto.response.StudentResponseDTO;

import java.io.IOException;

public interface ResumeCreator {

    byte[] createResumeToPdf(Long id) throws DocumentException;

}
