package uz.student.service.impl;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uz.student.dto.response.StudentResponseDTO;
import uz.student.model.Student;
import uz.student.repository.StudentRepository;
import uz.student.service.ExportService;
import uz.student.service.FieldOfStudyService;
import uz.student.service.StudentService;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class ExportServiceImpl implements ExportService {
    private static final Logger log = LoggerFactory.getLogger(ExportServiceImpl.class);
    private final StudentRepository studentRepository;
    private final FieldOfStudyService fieldOfStudyService;
    private final FileStorageService fileStorageService;
    private final StudentServiceImpl studentServiceImpl;
    private final StudentService studentService;

    public ExportServiceImpl(StudentRepository studentRepository, FieldOfStudyService fieldOfStudyService, FileStorageService fileStorageService, StudentServiceImpl studentServiceImpl, StudentService studentService) {
        this.studentRepository = studentRepository;

        this.fieldOfStudyService = fieldOfStudyService;
        this.fileStorageService = fileStorageService;
        this.studentServiceImpl = studentServiceImpl;
        this.studentService = studentService;
    }

    @Override
    public Page<StudentResponseDTO> getFilteredStudents(String firstName, String lastName, Pageable pageable) {
       Page<Student> students;
        if (firstName != null && lastName != null) {
            students = studentRepository.findByFirstNameContainingAndLastNameContaining(firstName, lastName, pageable);
        } else if (firstName != null) {
            students = studentRepository.findAllByFirstNameContains(firstName, pageable);
        } else if (lastName != null) {
            students = studentRepository.findAllByLastNameContains(lastName, pageable);
        } else {
            students = studentRepository.findAll(pageable);
        }

        return students.map(this::toDto);

    }

    @Override
    public void exportStudentToExcel(OutputStream outputStream) throws IOException {
        List<Student> students = studentRepository.findAll();
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Students Information");
        Row row = sheet.createRow(0);
        row.createCell(0).setCellValue("Id");
        row.createCell(1).setCellValue("First Name");
        row.createCell(2).setCellValue("Last Name");
        row.createCell(3).setCellValue("Middle Name");
        row.createCell(4).setCellValue("Description");
        row.createCell(5).setCellValue("Study Start Date");
        row.createCell(6).setCellValue("Study End Date");
        row.createCell(7).setCellValue("Gender");
        row.createCell(8).setCellValue("BirthDate");
        row.createCell(9).setCellValue("Created At");
        row.createCell(10).setCellValue("Field Of Study");
        row.createCell(11).setCellValue("University");

        for (int i = 0; i < students.size(); i++) {
            Row dataRow = sheet.createRow(i+ 1);
            dataRow.createCell(0).setCellValue(students.get(i).getId());
            dataRow.createCell(1).setCellValue(students.get(i).getFirstName());
            dataRow.createCell( 2).setCellValue(students.get(i).getLastName());
            dataRow.createCell( 3).setCellValue(students.get(i).getMiddleName());
            dataRow.createCell(4).setCellValue(students.get(i).getDescription());
            dataRow.createCell(5).setCellValue(students.get(i).getStudyStartDate());
            dataRow.createCell(6).setCellValue(students.get(i).getStudyEndDate());
            dataRow.createCell(7).setCellValue(students.get(i).getGender());
            dataRow.createCell(8).setCellValue(students.get(i).getBirthDate());
            dataRow.createCell(9).setCellValue(students.get(i).getCreatedAt());
            dataRow.createCell(10).setCellValue(students.get(i).getFieldOfStudy().getName());
            dataRow.createCell(11).setCellValue(students.get(i).getFieldOfStudy().getUniversity().getName());

        }
        workbook.write(outputStream);
        workbook.close();

    }

    @Override
    public byte[] exportStudentToPdf(Pageable pageable) throws IOException {

        try {

            Document document = new Document();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            PdfWriter.getInstance(document, bos);
            document.open();
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, BaseColor.BLACK);
            Paragraph title = new Paragraph("Student's List", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(Chunk.NEWLINE);
            Font tableHeaderFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, BaseColor.BLACK);
            Font tableBodyFont = FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.BLACK);
            PdfPTable table = new PdfPTable(12);


            table.addCell(new Phrase("ID", tableHeaderFont));
            table.addCell(new Phrase("First Name", tableHeaderFont));
            table.addCell(new Phrase("Last Name", tableHeaderFont));
            table.addCell(new Phrase("Middle Name", tableHeaderFont));
            table.addCell(new Phrase("Description", tableHeaderFont));
            table.addCell(new Phrase("Study State Date", tableHeaderFont));
            table.addCell(new Phrase("Study End Date", tableHeaderFont));
            table.addCell(new Phrase("Gender"));
            table.addCell(new Phrase("BirthDate", tableHeaderFont));
            table.addCell(new Phrase("Created At", tableHeaderFont));
            table.addCell(new Phrase("Field of Study", tableHeaderFont));
            table.addCell(new Phrase("University Name", tableHeaderFont));
            studentService.getAll().forEach(studentResponseDTO -> {
                table.addCell(new Phrase(studentResponseDTO.getId().toString(), tableHeaderFont));
                table.addCell(new Phrase(studentResponseDTO.getFirstName(), tableHeaderFont));
                table.addCell(new Phrase(studentResponseDTO.getLastName(), tableHeaderFont));
                table.addCell(new Phrase(studentResponseDTO.getMiddleName(), tableHeaderFont));
                table.addCell(new Phrase(studentResponseDTO.getDescription(), tableHeaderFont));
                table.addCell(new Phrase(studentResponseDTO.getStudyStartDate(), tableHeaderFont));
                table.addCell(new Phrase(studentResponseDTO.getStudyEndDate(), tableHeaderFont));
                table.addCell(new Phrase(studentResponseDTO.getGender(), tableHeaderFont));
                table.addCell(new Phrase(studentResponseDTO.getBirthDate(), tableHeaderFont));
                table.addCell(new Phrase(studentResponseDTO.getCreatedAt(), tableHeaderFont));
                table.addCell(new Phrase(studentResponseDTO.getFieldName(),tableBodyFont));
                table.addCell(new Phrase(studentResponseDTO.getUniversityName(), tableHeaderFont));
                    });
            document.add(table);
        document.close();
        log.info("PDF created successfully" );
        return bos.toByteArray();

        } catch (DocumentException e) {
            throw new IOException(e.getMessage());

        }
    }

    private StudentResponseDTO getOneStudentById(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Student not found with id: " + id));
        return toDto(student);
    }

    @Override
    public void setStudentAvatar(Long studentId, MultipartFile multipartFile) throws IOException {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found with id: " + studentId));
//        FileStorage fileStorage = fileStorageService.save(multipartFile);
        studentRepository.save(student);

    }

//    @Override
//    public byte[] createResumeToPdf(Long id) throws DocumentException {
//
//        try  {
//            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//            Document document = new Document();
//            PdfWriter.getInstance(document, outputStream);
//            document.open();
//
//            PdfPTable dataTimetable = new PdfPTable(1);
//            PdfPCell dataTimeCell = new PdfPCell();
//            SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            String currentDateTime = dateTimeFormat.format(new Date());
//            dataTimeCell.addElement(new Paragraph(currentDateTime, FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, BaseColor.DARK_GRAY)));
//            dataTimeCell.setBorder(Rectangle.NO_BORDER);
//            dataTimetable.addCell(dataTimeCell);
//            dataTimetable.setWidthPercentage(100);
//            dataTimetable.setHorizontalAlignment(Element.ALIGN_LEFT);
//            document.add(dataTimetable);
//
////            com.itextpdf.text.Image img = com.itextpdf.text.Image.getInstance(path);
////            img.setAbsolutePosition(473f, 750f);
////            img.scaleAbsolute(80f, 85);
//            PdfPTable table = new PdfPTable(2);
////            document.add(img);
//            document.add(new Paragraph(" ",
//                    FontFactory.getFont(FontFactory.TIMES_BOLD, 18, BaseColor.DARK_GRAY)));
//            document.add(new Paragraph(" ",
//                    FontFactory.getFont(FontFactory.TIMES_BOLD, 18, BaseColor.DARK_GRAY)));
////            document.add(new Paragraph("Name: " + student.getFirstName() + " " +
////                    student.getLastName() + " " + student.getMiddleName(),
//                    FontFactory.getFont(FontFactory.TIMES_BOLD, 18, BaseColor.DARK_GRAY);
//            document.add(new Paragraph("",
//                    FontFactory.getFont(FontFactory.TIMES_BOLD, 18, BaseColor.DARK_GRAY)));
//            document.add(new Paragraph("",
//                    FontFactory.getFont(FontFactory.TIMES_BOLD, 18, BaseColor.DARK_GRAY)));
//
//            document.add(new Paragraph("----------------------------------------------------------------------------------------------------------------------------------"));
//            document.add(new Paragraph("CONTACT DETAILS", FontFactory.getFont(FontFactory.TIMES_BOLD, 9, com.itextpdf.text.Font.BOLD, BaseColor.DARK_GRAY)));
//            document.add(new Paragraph("Email: " + " " + " ", FontFactory.getFont(FontFactory.TIMES_BOLD, 7, BaseColor.DARK_GRAY)));
//            document.add(new Paragraph("Contact", FontFactory.getFont(FontFactory.TIMES_BOLD, 7, BaseColor.DARK_GRAY)));
//            document.add(new Paragraph("Address", FontFactory.getFont(FontFactory.TIMES_BOLD, 7, BaseColor.DARK_GRAY)));
//            document.add(new Paragraph("----------------------------------------------------------------------------------------------------------------------------------"));
//            document.add(new Paragraph("SKILLS", FontFactory.getFont(FontFactory.TIMES_BOLD, 9, com.itextpdf.text.Font.BOLD, BaseColor.DARK_GRAY)));
//            //Skills
//            document.add(new Paragraph("Skill 1", FontFactory.getFont(FontFactory.TIMES_BOLD, 7, BaseColor.DARK_GRAY)));
//            document.add(new Paragraph("Skill 2", FontFactory.getFont(FontFactory.TIMES_BOLD, 7, BaseColor.DARK_GRAY)));
//            document.add(new Paragraph("Skill 3", FontFactory.getFont(FontFactory.TIMES_BOLD, 7, BaseColor.DARK_GRAY)));
//            document.add(new Paragraph("Skill 4", FontFactory.getFont(FontFactory.TIMES_BOLD, 7, BaseColor.DARK_GRAY)));
//            document.add(table);
//            document.add(new Paragraph("----------------------------------------------------------------------------------------------------------------------------------"));
//            document.add(new Paragraph("QUALIFICATIONS", FontFactory.getFont(FontFactory.TIMES_BOLD, 9, com.itextpdf.text.Font.BOLD, BaseColor.DARK_GRAY)));
//            document.add(new Paragraph("Collage: ", FontFactory.getFont(FontFactory.TIMES_BOLD, 7, BaseColor.DARK_GRAY)));
////            document.add(new Paragraph("University: " + " " + student.getFieldOfStudy().getUniversity(), FontFactory.getFont(FontFactory.TIMES_BOLD, 7, BaseColor.DARK_GRAY)));
//            document.add(new Paragraph("e.t.c", FontFactory.getFont(FontFactory.TIMES_BOLD, 7, BaseColor.DARK_GRAY)));
//            document.add(new Paragraph("----------------------------------------------------------------------------------------------------------------------------------"));
//            document.add(new Paragraph("WORK EXPERIENCE", FontFactory.getFont(FontFactory.TIMES_BOLD, 10, com.itextpdf.text.Font.BOLD, BaseColor.DARK_GRAY)));
//            document.add(new Paragraph("DAVR BANK", FontFactory.getFont(FontFactory.TIMES_BOLD, 7, BaseColor.DARK_GRAY)));
//            document.add(new Paragraph("----------------------------------------------------------------------------------------------------------------------------------"));
//            document.add(new Paragraph("REFERENCES", FontFactory.getFont(FontFactory.TIMES_BOLD, 9, com.itextpdf.text.Font.BOLD, BaseColor.DARK_GRAY)));
//            document.add(new Paragraph("Available upon request", FontFactory.getFont(FontFactory.TIMES_BOLD, 6, BaseColor.DARK_GRAY)));
//            document.close();
//            return outputStream.toByteArray();
//
//
//        } catch (DocumentException e) {
//            throw new RuntimeException("Error generating PDF: " + e.getMessage(), e);
//        }
//    }

    private StudentResponseDTO toDto(Student student) {
            StudentResponseDTO studentResponseDTO = new StudentResponseDTO();
            studentResponseDTO.setFirstName(student.getFirstName());
            studentResponseDTO.setLastName(student.getLastName());
            studentResponseDTO.setMiddleName(student.getMiddleName());
            studentResponseDTO.setDescription(student.getDescription());
            studentResponseDTO.setStudyStartDate(student.getStudyStartDate());
            studentResponseDTO.setStudyEndDate(student.getStudyEndDate());
            studentResponseDTO.setGender(student.getGender());
            studentResponseDTO.setBirthDate(student.getBirthDate());
            studentResponseDTO.setCreatedAt(student.getCreatedAt());
            studentResponseDTO.setFieldName(student.getFieldOfStudy().getName());
            studentResponseDTO.setId(student.getId());
            return studentResponseDTO;
        }
    }

