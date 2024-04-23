package uz.student.service.impl;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uz.student.dto.response.StudentResponseDTO;
import uz.student.model.FileStorage;
import uz.student.model.Student;
import uz.student.repository.StudentRepository;
import uz.student.service.ExportService;
import uz.student.service.FieldOfStudyService;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

@Service
public class ExportServiceImpl implements ExportService {
    private final StudentRepository studentRepository;
    private final FieldOfStudyService fieldOfStudyService;

    public ExportServiceImpl(StudentRepository studentRepository, FieldOfStudyService fieldOfStudyService) {
        this.studentRepository = studentRepository;

        this.fieldOfStudyService = fieldOfStudyService;
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
    public void exportStudentToPdf(Long id, OutputStream outputStream) throws IOException {
        StudentResponseDTO student = getOneStudentById(id);
        try {
            Document document = new Document();
            PdfWriter.getInstance(document, outputStream);

            document.open();
            document.add(new Paragraph("Student Information"));
            document.add(new Paragraph("ID: " + student.getId()));
            document.add(new Paragraph("First Name: " + student.getFirstName()));
            document.add(new Paragraph("Last Name: " + student.getLastName()));
            document.add(new Paragraph("Middle Name: " + student.getMiddleName()));
            document.add(new Paragraph("Description: " + student.getDescription()));
            document.add(new Paragraph("Study Start Date: " + student.getStudyStartDate()));
            document.add(new Paragraph("Study End Date: " + student.getStudyEndDate()));
            document.add(new Paragraph("Gender: " + student.getGender()));
            document.add(new Paragraph("Birth Date: " + student.getBirthDate()));
            document.add(new Paragraph("Created At: " + student.getCreatedAt()));
            document.add(new Paragraph("Field of Study: " + student.getFieldName()));
            document.close();

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
        FileStorage fileStorage = fieldOfStudyService.save(multipartFile);
        student.setAvatarUrl(fileStorage.getUploadFolder());
        studentRepository.save(student);

    }

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

