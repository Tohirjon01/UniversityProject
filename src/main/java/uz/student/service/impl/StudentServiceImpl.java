package uz.student.service.impl;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import uz.student.dto.request.StudentRequestDTO;
import uz.student.dto.response.ResponseDTO;
import uz.student.dto.response.StudentResponseDTO;
import uz.student.model.FieldOfStudy;
import uz.student.model.FileStorage;
import uz.student.model.Student;
import uz.student.repository.FieldOfStudyRepository;
import uz.student.repository.StudentRepository;
import uz.student.service.StudentService;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {
    private static final Logger log = LoggerFactory.getLogger(StudentServiceImpl.class);
    private final StudentRepository studentRepository;


    public StudentServiceImpl(StudentRepository studentRepository, FieldOfStudyRepository fieldOfStudyRepository) {
        this.studentRepository = studentRepository;
        this.fieldOfStudyRepository = fieldOfStudyRepository;
    }

    private final FieldOfStudyRepository fieldOfStudyRepository;

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

        return toDtos(studentList);
    }

    @Override
    public void setStudentAvatar(Long id, MultipartFile file) {

    }

    private StudentResponseDTO toDto (Student student){
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

        private Student toEntity (StudentRequestDTO studentRequestDTO){
            Student student = new Student();
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

        private List<StudentResponseDTO> toDtos (List < Student > studentList) {
            List<StudentResponseDTO> studentResponseDTOS = new ArrayList<>();
            for (int i = 0; i < studentList.size(); i++) {
                StudentResponseDTO studentResponseDTO = new StudentResponseDTO();
                FieldOfStudy fieldOfStudy = fieldOfStudyRepository.findById(studentList.get(i).getFieldOfStudy().getId()).get();
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

        private void updateFromDto (StudentRequestDTO studentRequestDTO, Student student){
            if (studentRequestDTO.getFirstName() != null) {
                student.setFirstName(studentRequestDTO.getFirstName());
            }
            if (studentRequestDTO.getLastName() != null) {
                student.setLastName(studentRequestDTO.getLastName());
            }
            if (studentRequestDTO.getMiddleName() != null) {
                student.setMiddleName(studentRequestDTO.getMiddleName());
            }
            if (studentRequestDTO.getDescription() != null) {
                student.setDescription(studentRequestDTO.getDescription());
            }
            if (studentRequestDTO.getStudyStartDate() != null) {
                student.setStudyStartDate(studentRequestDTO.getStudyStartDate());
            }
            if (studentRequestDTO.getStudyEndDate() != null) {
                student.setStudyEndDate(studentRequestDTO.getStudyEndDate());
            }
            if (studentRequestDTO.getGender() != null) {
                student.setGender(studentRequestDTO.getGender());
            }
            if (studentRequestDTO.getBirthDate() != null) {
                student.setBirthDate(studentRequestDTO.getBirthDate());
            }
            if (studentRequestDTO.getCreatedAt() != null) {
                student.setCreatedAt(studentRequestDTO.getCreatedAt());
            }
            if (studentRequestDTO.getFieldOfStudy() != null) {
                student.getFieldOfStudy().setId(studentRequestDTO.getFieldOfStudy().getId());
            }


        }

    }
