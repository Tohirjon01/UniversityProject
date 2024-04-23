package uz.student.service;

import jakarta.annotation.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;

public interface StorageService {
    String store(MultipartFile file) throws IOException;
    Resource load(String filename)throws MalformedURLException;
    void delete(String hashId) throws IOException;



}
