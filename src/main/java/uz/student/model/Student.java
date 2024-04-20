package uz.student.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Student implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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


    @ManyToOne
    private FieldOfStudy fieldOfStudy;

}