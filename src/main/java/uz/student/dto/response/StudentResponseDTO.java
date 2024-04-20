package uz.student.dto.response;

import java.util.ArrayList;
import java.util.List;

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

    public StudentResponseDTO() {
    }

    public StudentResponseDTO(Long id, String firstName, String lastName, String middleName, String description,
                              String studyStartDate, String studyEndDate, String gender, String birthDate,
                              String createdAt, String fieldName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
        this.description = description;
        this.studyStartDate = studyStartDate;
        this.studyEndDate = studyEndDate;
        this.gender = gender;
        this.birthDate = birthDate;
        this.createdAt = createdAt;
        this.fieldName = fieldName;
    }



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

}
