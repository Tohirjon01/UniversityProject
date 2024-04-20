package uz.student.service.impl;

import org.hashids.Hashids;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uz.student.enumeration.FileStorageStatus;
import uz.student.model.FileStorage;
import uz.student.repository.FileStorageRepository;

import java.io.File;
import java.io.IOException;
import java.util.Date;

@Service
public class FileStorageService {
    private final FileStorageRepository fileStorageRepository;
    @Value("${upload.server.folder}")
    private String serverFolderPath;
    private final Hashids hashids;

    public FileStorageService(FileStorageRepository fileStorageRepository) {
        this.fileStorageRepository = fileStorageRepository;
        this.hashids = new Hashids(getClass().getName(),6);
    }

    public FileStorage save(MultipartFile multipartFile) {
        FileStorage fileStorage = new FileStorage();
        fileStorage.setName(multipartFile.getName());
        fileStorage.setFileSize(multipartFile.getSize());
        fileStorage.setContentType(multipartFile.getContentType());
        fileStorage.setExtension(getExtension(multipartFile.getOriginalFilename()));
        fileStorage.setFileStorageStatus(FileStorageStatus.DRAFT);
        fileStorage = fileStorageRepository.save(fileStorage);

        //   /serverFolderPath/upload_folder/year/month/day/sdadda.pdf(.....)

        Date now = new Date();
        String path = String.format("%s/upload_files/%d/%d/%d",
                this.serverFolderPath,
                1900+now.getYear(),
                1 + now.getMonth() ,
                now.getDate());

        File uploadFolder = new File(path);
        if (!uploadFolder.exists() && uploadFolder.mkdirs()){
            System.out.println("folder created");
        }

        fileStorage.setHashId(hashids.encode(fileStorage.getId()));
        String pathLocal = String.format("/upload_file/%d/%d/%d/%s.%s",
                1900 + now.getYear(),
                1 + now.getMonth(),
                now.getDate(),
                fileStorage.getHashId(),
                fileStorage.getExtension());
        fileStorage.setUploadFolder(pathLocal);
        fileStorageRepository.save(fileStorage);
        uploadFolder = uploadFolder.getAbsoluteFile();
        File file = new File(uploadFolder , String.format("%s.%s",
                fileStorage.getHashId(),
                fileStorage.getExtension()));
        try {
            multipartFile.transferTo(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        return fileStorage;
    }
    public FileStorage findByHashId(String hashId) {
        return fileStorageRepository.findByHashId(hashId);
    }
    public  void  delete(String hashId){
        FileStorage fileStorage = fileStorageRepository.findByHashId(hashId);
        if (fileStorage == null) {
            throw new RuntimeException("File not found in database");
        }

        String filePath = String.format("%s/%s", this.serverFolderPath, fileStorage.getUploadFolder());
        File file = new File(filePath);

        if (!file.exists()) {
            throw new RuntimeException("File not found at specified path: " + filePath);
        }
        try {
            if (file.delete()) {
                fileStorageRepository.delete(fileStorage);
            } else {
                throw new RuntimeException("Failed to delete file. Check file permissions or if the file is being used by another process");
            }
        } catch (SecurityException e) {
            throw new RuntimeException("Permission denied while deleting file: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Error deleting file: " + e.getMessage(), e);
        }
    }

    private String getExtension(String fileName) {
        //kkkkk.doc
        String ext = null;
        if (fileName != null && !fileName.isEmpty()) {
            int dot = fileName.lastIndexOf('.');
            if (dot > 0 && dot <= fileName.length() - 2) {
                ext=fileName.substring(dot+1);
            }
        }
        return ext;
    }


}
