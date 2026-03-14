package org.instagram.postservice.service;

import org.instagram.postservice.exception.BadRequestException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Service
public class FileStorageService {

    @Value("${file.upload.dir:uploads/posts}")
    private String uploadDir;

    private static final Set<String> ALLOWED_EXTENSIONS = new HashSet<>(Arrays.asList(
            "jpg", "jpeg", "png", "gif", "mp4", "mov", "avi", "webm"
    ));

    private static final long MAX_FILE_SIZE = 50 * 1024 * 1024; // 50MB

    public String storeFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new BadRequestException("File is empty");
        }

        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || !isValidFileExtension(originalFilename)) {
            throw new BadRequestException("Invalid file type. Allowed types: jpg, jpeg, png, gif, mp4, mov, avi, webm");
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new BadRequestException("File size exceeds maximum allowed size of 50MB");
        }

        try {
            File uploadDirectory = new File(uploadDir);
            if (!uploadDirectory.exists()) {
                uploadDirectory.mkdirs();
            }

            String fileExtension = getFileExtension(originalFilename);
            String filename = UUID.randomUUID() + "." + fileExtension;

            Path filepath = Paths.get(uploadDir, filename);
            Files.write(filepath, file.getBytes());

            return filename;
        } catch (IOException e) {
            throw new BadRequestException("Failed to store file: " + e.getMessage());
        }
    }

    public byte[] getFile(String filename) {
        try {
            Path filepath = Paths.get(uploadDir, filename);
            if (!Files.exists(filepath)) {
                throw new BadRequestException("File not found: " + filename);
            }
            return Files.readAllBytes(filepath);
        } catch (IOException e) {
            throw new BadRequestException("Failed to read file: " + e.getMessage());
        }
    }

    public void deleteFile(String filename) {
        try {
            Path filepath = Paths.get(uploadDir, filename);
            Files.deleteIfExists(filepath);
        } catch (IOException e) {
            // Log error but don't throw - file might already be deleted
            System.err.println("Failed to delete file: " + e.getMessage());
        }
    }

    private boolean isValidFileExtension(String filename) {
        String extension = getFileExtension(filename);
        return ALLOWED_EXTENSIONS.contains(extension.toLowerCase());
    }

    private String getFileExtension(String filename) {
        int lastIndex = filename.lastIndexOf('.');
        if (lastIndex > 0) {
            return filename.substring(lastIndex + 1);
        }
        return "";
    }
}
