package uz.student.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import uz.student.dto.request.StudentRequestDTO;
import uz.student.dto.response.ResponseDTO;
import uz.student.dto.response.StudentResponseDTO;
import uz.student.model.Student;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public interface StudentService {
    Page<Student> getAllStudents(Pageable pageable);
    Page<StudentResponseDTO> getFilteredStudents(String firstName, String lastName, Pageable pageable);
    StudentResponseDTO createStudentInfo(StudentRequestDTO studentRequestDTO);
    StudentResponseDTO updateStudentInfo(StudentRequestDTO studentRequestDTO , Long id);

    StudentResponseDTO getOneStudentById(Long id);

    ResponseDTO deleteStudentById(Long id);
    List<StudentResponseDTO> getAll();
    void exportStudentToPdf(Long id, OutputStream outputStream) throws IOException;
    void exportStudentToExcel(Long id, OutputStream outputStream) throws IOException;
    void deleteStudent(Long id);





}
