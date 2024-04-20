package uz.student.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import uz.student.model.FieldOfStudy;

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
    @JsonProperty("field_Of_Study")
    private FieldOfStudy fieldOfStudy;

    public FieldOfStudy getFieldOfStudy() {
        return fieldOfStudy;
    }

    public StudentRequestDTO(String firstName, String lastName, String middleName, String description,
                             String studyStartDate, String studyEndDate, String gender, String birthDate,
                             String createdAt, FieldOfStudy fieldOfStudy) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
        this.description = description;
        this.studyStartDate = studyStartDate;
        this.studyEndDate = studyEndDate;
        this.gender = gender;
        this.birthDate = birthDate;
        this.createdAt = createdAt;
        this.fieldOfStudy = fieldOfStudy;
    }

    @Override
    public String toString() {
        return "StudentRequestDTO{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", middleName='" + middleName + '\'' +
                ", description='" + description + '\'' +
                ", studyStartDate='" + studyStartDate + '\'' +
                ", studyEndDate='" + studyEndDate + '\'' +
                ", gender='" + gender + '\'' +
                ", birthDate='" + birthDate + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", fieldOfStudy=" + fieldOfStudy +
                '}';
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStudyStartDate() {
        return studyStartDate;
    }

    public void setStudyStartDate(String studyStartDate) {
        this.studyStartDate = studyStartDate;
    }

    public String getStudyEndDate() {
        return studyEndDate;
    }

    public void setStudyEndDate(String studyEndDate) {
        this.studyEndDate = studyEndDate;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public void setFieldOfStudy(FieldOfStudy fieldOfStudy) {
        this.fieldOfStudy = fieldOfStudy;
    }
}
