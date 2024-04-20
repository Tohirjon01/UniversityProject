package uz.student.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.student.model.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student , Long> {
    Page<Student> findByFirstNameContainingAndLastNameContaining(String firstName, String lastName, Pageable pageable);

    Page<Student> findAllByFirstNameContains(String firstName, Pageable pageable);

    Page<Student> findAllByLastNameContains(String lastName, Pageable pageable);


}
