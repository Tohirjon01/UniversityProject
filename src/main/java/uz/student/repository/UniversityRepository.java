package uz.student.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.student.model.University;

import java.beans.JavaBean;

@Repository
public interface UniversityRepository extends JpaRepository<University,Long> {

}
