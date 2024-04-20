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
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.student.dto.request.StudentRequestDTO;
import uz.student.dto.response.ResponseDTO;
import uz.student.dto.response.StudentResponseDTO;
import uz.student.model.FieldOfStudy;
import uz.student.model.Student;
import uz.student.repository.FieldOfStudyRepository;
import uz.student.repository.StudentRepository;
import uz.student.service.StudentService;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {
    private final StudentRepository studentRepository;

    public StudentServiceImpl(StudentRepository studentRepository, FieldOfStudyRepository fieldOfStudyRepository) {
        this.studentRepository = studentRepository;
        this.fieldOfStudyRepository = fieldOfStudyRepository;
    }

    private final FieldOfStudyRepository fieldOfStudyRepository;


    @Override
    @Transactional(readOnly = true)
    public Page<Student> getAllStudents(Pageable pageable) {
        return studentRepository.findAll(pageable);
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
    public StudentResponseDTO createStudentInfo(StudentRequestDTO studentRequestDTO) {
        Student student = toEntity(studentRequestDTO);
        student = studentRepository.save(student);
        FieldOfStudy fieldOfStudy = fieldOfStudyRepository.findById(student.getFieldOfStudy().getId()).get();
        StudentResponseDTO studentResponseDTO = toDto(student);
        studentResponseDTO.setFieldName(fieldOfStudy.getName());


        return studentResponseDTO;
    }

    @Override
    public StudentResponseDTO updateStudentInfo(StudentRequestDTO studentRequestDTO, Long id) {
        Student student = studentRepository.findById(id).get();
        FieldOfStudy fieldOfStudy = fieldOfStudyRepository.findById(student.getFieldOfStudy().getId()).get();
        updateFromDto(studentRequestDTO, student);
        student = studentRepository.save(student);
        StudentResponseDTO studentResponseDTO = toDto(student);
        studentResponseDTO.setFieldName(fieldOfStudy.getName());

        return studentResponseDTO;
    }

    @Override
    public StudentResponseDTO getOneStudentById(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Student not found with id: " + id));
        return toDto(student);
    }

    @Override
    public ResponseDTO deleteStudentById(Long id) {
        studentRepository.deleteById(id);
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setMessage(id + " Id User with this id has been deleted");
        return responseDTO;
    }

    @Override
    public List<StudentResponseDTO> getAll() {
        List<Student> studentList = studentRepository.findAll();
        List<StudentResponseDTO> fieldResponseDTO = toDtos(studentList);

        return fieldResponseDTO;
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
            throw new IOException("Error generating PDF", e);
        }
    }


    @Override
    public void exportStudentToExcel(Long id, OutputStream outputStream) throws IOException {
        StudentResponseDTO student = getOneStudentById(id);
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Student Information");
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("ID");
        headerRow.createCell(1).setCellValue("First Name");
        headerRow.createCell(2).setCellValue("Last Name");
        headerRow.createCell(3).setCellValue("Middle Name");
        headerRow.createCell(4).setCellValue("Description");
        headerRow.createCell(5).setCellValue("Study Start Date");
        headerRow.createCell(6).setCellValue("Study End Date");
        headerRow.createCell(7).setCellValue("Gender");
        headerRow.createCell(8).setCellValue("Birth Date");
        headerRow.createCell(9).setCellValue("Created At");
        headerRow.createCell(10).setCellValue("Field of Study");

        Row dataRow = sheet.createRow(1);
        dataRow.createCell(0).setCellValue(student.getId());
        dataRow.createCell(1).setCellValue(student.getFirstName());
        dataRow.createCell(2).setCellValue(student.getLastName());
        dataRow.createCell(3).setCellValue(student.getMiddleName());
        dataRow.createCell(4).setCellValue(student.getDescription());
        dataRow.createCell(5).setCellValue(student.getStudyStartDate());
        dataRow.createCell(6).setCellValue(student.getStudyEndDate());
        dataRow.createCell(7).setCellValue(student.getGender());
        dataRow.createCell(8).setCellValue(student.getBirthDate());
        dataRow.createCell(9).setCellValue(student.getCreatedAt());
        dataRow.createCell(10).setCellValue(student.getFieldName());

        workbook.write(outputStream);
        workbook.close();
    }

    @Override
    public void deleteStudent(Long id) {

    }


    private StudentResponseDTO toDto(Student student){
        StudentResponseDTO studentResponseDTO=new StudentResponseDTO();
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

private Student toEntity(StudentRequestDTO studentRequestDTO){
        Student student=new Student();
        student.setFirstName(studentRequestDTO.getFirstName());
        student.setLastName(studentRequestDTO.getLastName());
        student.setMiddleName(studentRequestDTO.getMiddleName());
        student.setDescription(studentRequestDTO.getDescription());
        student.setStudyStartDate(studentRequestDTO.getStudyStartDate());
        student.setStudyEndDate(studentRequestDTO.getStudyEndDate());
        student.setGender(studentRequestDTO.getGender());
        student.setBirthDate(studentRequestDTO.getBirthDate());
        student.setCreatedAt(studentRequestDTO.getCreatedAt());
        student.setFieldOfStudy(studentRequestDTO.getFieldOfStudy());
        return student;
        }

private List<StudentResponseDTO> toDtos(List<Student> studentList){
        List<StudentResponseDTO> studentResponseDTOS=new ArrayList<>();
        for(int i=0;i<studentList.size();i++){
        StudentResponseDTO studentResponseDTO=new StudentResponseDTO();
        FieldOfStudy fieldOfStudy=fieldOfStudyRepository.findById(studentList.get(i).getFieldOfStudy().getId()).get();
        studentResponseDTO.setFirstName(studentList.get(i).getFirstName());
        studentResponseDTO.setLastName(studentList.get(i).getLastName());
        studentResponseDTO.setMiddleName(studentList.get(i).getMiddleName());
        studentResponseDTO.setDescription(studentList.get(i).getDescription());
        studentResponseDTO.setStudyStartDate(studentList.get(i).getStudyStartDate());
        studentResponseDTO.setStudyEndDate(studentList.get(i).getStudyEndDate());
        studentResponseDTO.setGender(studentList.get(i).getGender());
        studentResponseDTO.setBirthDate(studentList.get(i).getBirthDate());
        studentResponseDTO.setCreatedAt(studentList.get(i).getCreatedAt());
        studentResponseDTO.setFieldName(fieldOfStudy.getName());
        studentResponseDTO.setId(studentList.get(i).getId());
        studentResponseDTOS.add(studentResponseDTO);


        }
        return studentResponseDTOS;
        }

private void updateFromDto(StudentRequestDTO studentRequestDTO,Student student){
        if(studentRequestDTO.getFirstName()!=null){
        student.setFirstName(studentRequestDTO.getFirstName());
        }
        if(studentRequestDTO.getLastName()!=null){
        student.setLastName(studentRequestDTO.getLastName());
        }
        if(studentRequestDTO.getMiddleName()!=null){
        student.setMiddleName(studentRequestDTO.getMiddleName());
        }
        if(studentRequestDTO.getDescription()!=null){
        student.setDescription(studentRequestDTO.getDescription());
        }
        if(studentRequestDTO.getStudyStartDate()!=null){
        student.setStudyStartDate(studentRequestDTO.getStudyStartDate());
        }
        if(studentRequestDTO.getStudyEndDate()!=null){
        student.setStudyEndDate(studentRequestDTO.getStudyEndDate());
        }
        if(studentRequestDTO.getGender()!=null){
        student.setGender(studentRequestDTO.getGender());
        }
        if(studentRequestDTO.getBirthDate()!=null){
        student.setBirthDate(studentRequestDTO.getBirthDate());
        }
        if(studentRequestDTO.getCreatedAt()!=null){
        student.setCreatedAt(studentRequestDTO.getCreatedAt());
        }
        if(studentRequestDTO.getFieldOfStudy()!=null){
        student.getFieldOfStudy().setId(studentRequestDTO.getFieldOfStudy().getId());
        }


        }
        }
