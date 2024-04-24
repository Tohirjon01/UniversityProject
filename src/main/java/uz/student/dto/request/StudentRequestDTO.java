package uz.student.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import uz.student.model.FieldOfStudy;
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class StudentRequestDTO {

    @JsonProperty("first_name")
    private String firstName;
    @JsonProperty("last_Name")
    private String lastName;
    @JsonProperty("middle_Name")
    private String middleName;

    private String description;
    @JsonProperty("study_Start_Date")
    private String studyStartDate;
    @JsonProperty("study_End_Date")
    private String studyEndDate;
    private String gender;
    @JsonProperty("birth_Date")
    private String birthDate;
    @JsonProperty("created_At")
    private String createdAt;
    @JsonProperty("field_Of_study")
    private FieldOfStudy fieldOfStudy;


}
