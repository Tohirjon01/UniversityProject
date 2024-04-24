package uz.student.service.impl;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import uz.student.dto.response.StudentResponseDTO;
import uz.student.model.FileStorage;
import uz.student.model.Student;
import uz.student.repository.FileStorageRepository;
import uz.student.repository.StudentRepository;
import uz.student.service.ResumeCreator;
import uz.student.service.StudentService;

import javax.management.Query;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

@Service
public class ResumeCreatorImpl implements ResumeCreator {
    private final StudentRepository studentRepository;
    private final StudentService studentService;
    private final FileStorageRepository fileStorageRepository;
    @Value("${upload.server.folder}")
    private String uploadFolder;


    public ResumeCreatorImpl(StudentRepository studentRepository, StudentService studentService, FileStorageRepository fileStorageRepository) {
        this.studentRepository = studentRepository;
        this.studentService = studentService;
        this.fileStorageRepository = fileStorageRepository;
    }

    @Override
    public byte[] createResumeToPdf(Long id) throws DocumentException {
        Optional<Student> byId = studentRepository.findById(id);
        if (byId.isEmpty()) {
            throw new DocumentException("Student not found");
        }
        Student studentResponseDTO = byId.get();
        FileStorage fileStorage = fileStorageRepository.findById(studentResponseDTO.getFileStorage().getId()).get();
        String path = "D:\\GitHub\\UniversityProject1\\" + this.uploadFolder +"\\" + fileStorage.getUploadFolder();
        System.out.println(path);
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            Document document = new Document();
            PdfWriter.getInstance(document, outputStream);
            document.open();
            PdfPTable table = new PdfPTable(1);
            PdfPCell dateTimeCell = new PdfPCell();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            String currentDateTime = dateFormat.format(new Date());
            dateTimeCell.addElement(new Paragraph(currentDateTime, FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, BaseColor.DARK_GRAY)));
            dateTimeCell.setBorder(Rectangle.NO_BORDER);
            table.addCell(dateTimeCell);
            table.setWidthPercentage(100);
            table.setHorizontalAlignment(Element.ALIGN_LEFT);
            document.add(dateTimeCell);

            com.itextpdf.text.Image img = com.itextpdf.text.Image.getInstance(path);
            img.setAbsolutePosition(437f,750f);
            img.scaleAbsolute(80f,85);
            PdfPTable table2 = new PdfPTable(2);
            document.add(img);
            document.add(new Paragraph("",
                    FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, BaseColor.DARK_GRAY)));
            document.add(new Paragraph("",
                    FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, BaseColor.DARK_GRAY)));
            document.add(new Paragraph("Name " +
                    studentResponseDTO.getFirstName() + " " +
                    studentResponseDTO.getLastName() + " " +
                    studentResponseDTO.getMiddleName(), FontFactory.getFont(FontFactory.TIMES_ROMAN,18,BaseColor.DARK_GRAY)));
            document.add(new Paragraph("",
                    FontFactory.getFont(FontFactory.TIMES_BOLD, 18, BaseColor.DARK_GRAY)));
            document.add(new Paragraph("",
                    FontFactory.getFont(FontFactory.TIMES_BOLD, 18, BaseColor.DARK_GRAY)));

            document.add(new Paragraph("--------------------------------------------------------------------------------------------------"));
            document.add(new Paragraph("CONTACT DETAILS", FontFactory.getFont(FontFactory.TIMES_BOLD, 10, com.itextpdf.text.Font.BOLD, BaseColor.DARK_GRAY)));
            document.add(new Paragraph("Email: " + " " + " ", FontFactory.getFont(FontFactory.TIMES_BOLD, 7, BaseColor.DARK_GRAY)));
            document.add(new Paragraph("Contact", FontFactory.getFont(FontFactory.TIMES_BOLD, 7, BaseColor.DARK_GRAY)));
            document.add(new Paragraph("Address", FontFactory.getFont(FontFactory.TIMES_BOLD, 7, BaseColor.DARK_GRAY)));
            document.add(new Paragraph("----------------------------------------------------------------------------------------------------------------------------------"));
            document.add(new Paragraph("SKILLS", FontFactory.getFont(FontFactory.TIMES_BOLD, 10, com.itextpdf.text.Font.BOLD, BaseColor.DARK_GRAY)));
            //Skills
            document.add(new Paragraph("Skill 1", FontFactory.getFont(FontFactory.TIMES_BOLD, 8, BaseColor.DARK_GRAY)));
            document.add(new Paragraph("Skill 2", FontFactory.getFont(FontFactory.TIMES_BOLD, 8, BaseColor.DARK_GRAY)));
            document.add(new Paragraph("Skill 3", FontFactory.getFont(FontFactory.TIMES_BOLD, 8, BaseColor.DARK_GRAY)));
            document.add(new Paragraph("Skill 4", FontFactory.getFont(FontFactory.TIMES_BOLD, 8, BaseColor.DARK_GRAY)));
            document.add(table2);
            document.add(new Paragraph("----------------------------------------------------------------------------------------------------------------------------------"));
            document.add(new Paragraph("QUALIFICATIONS", FontFactory.getFont(FontFactory.TIMES_BOLD, 10, com.itextpdf.text.Font.BOLD, BaseColor.DARK_GRAY)));
            document.add(new Paragraph("Collage: ", FontFactory.getFont(FontFactory.TIMES_BOLD, 7, BaseColor.DARK_GRAY)));
            document.add(new Paragraph("University: " +
                    " " + studentResponseDTO.getFieldOfStudy().getUniversity().getName(), FontFactory.getFont(FontFactory.TIMES_BOLD, 7, BaseColor.DARK_GRAY)));
            document.add(new Paragraph("e.t.c", FontFactory.getFont(FontFactory.TIMES_BOLD, 7, BaseColor.DARK_GRAY)));
            document.add(new Paragraph("----------------------------------------------------------------------------------------------------------------------------------"));
            document.add(new Paragraph("WORK EXPERIENCE", FontFactory.getFont(FontFactory.TIMES_BOLD, 10, com.itextpdf.text.Font.BOLD, BaseColor.DARK_GRAY)));
            document.add(new Paragraph("DAVR BANK", FontFactory.getFont(FontFactory.TIMES_BOLD, 7, BaseColor.DARK_GRAY)));
            document.add(new Paragraph("----------------------------------------------------------------------------------------------------------------------------------"));
            document.add(new Paragraph("REFERENCES", FontFactory.getFont(FontFactory.TIMES_BOLD, 10, com.itextpdf.text.Font.BOLD, BaseColor.DARK_GRAY)));
            document.add(new Paragraph("Available upon request", FontFactory.getFont(FontFactory.TIMES_BOLD, 7, BaseColor.DARK_GRAY)));
            document.close();
            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
