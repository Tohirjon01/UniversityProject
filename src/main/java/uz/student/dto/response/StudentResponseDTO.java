package uz.student.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentResponseDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String middleName;
    private String description;
    private String studyStartDate;
    private String studyEndDate;
    private String gender;
    private String birthDate;
    private String createdAt;
    private String fieldName;
    private String universityName;
}

