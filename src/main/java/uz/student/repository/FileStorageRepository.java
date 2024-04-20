package uz.student.repository;

import org.hibernate.sql.ast.tree.expression.JdbcParameter;
import org.springframework.data.jpa.repository.JpaRepository;
import uz.student.model.FileStorage;

import java.io.File;

public interface FileStorageRepository extends JpaRepository<FileStorage , Long> {
    FileStorage findByHashId(String hashId);
}
