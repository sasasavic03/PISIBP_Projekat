package org.instagram.postservice.service;

import org.instagram.postservice.exception.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileStorageService {

    @Autowired
    private MinioService minioService;


    public String storeFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BadRequestException("File is empty");
        }
        return minioService.uploadFile(file);
    }


    public byte[] getFile(String filename) {
        if (filename == null || filename.isEmpty()) {
            throw new BadRequestException("Filename cannot be empty");
        }
        return minioService.downloadFile(filename);
    }


    public void deleteFile(String filename) {
        if (filename == null || filename.isEmpty()) {
            return;
        }
        minioService.deleteFile(filename);
    }

    public String getContentType(String filename) {
        return minioService.getContentType(filename);
    }


    public long getFileSize(String filename) {
        return minioService.getFileSize(filename);
    }


    public boolean fileExists(String filename) {
        return minioService.fileExists(filename);
    }


    public String generatePresignedUrl(String filename, int expirySeconds) {
        return minioService.generatePresignedUrl(filename, expirySeconds);
    }
}
