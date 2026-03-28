package org.instagram.postservice.service;

import io.minio.*;
import io.minio.errors.MinioException;
import io.minio.http.Method;
import org.instagram.postservice.exception.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Service
public class MinioService {

    @Autowired
    private MinioClient minioClient;

    @Value("${minio.bucket-name}")
    private String bucketName;

    private static final Set<String> ALLOWED_EXTENSIONS = new HashSet<>(Arrays.asList(
            "jpg", "jpeg", "png", "gif", "mp4", "mov", "avi", "webm"
    ));

    private static final long MAX_FILE_SIZE = 50 * 1024 * 1024; // 50MB

    private static final Map<String, String> MEDIA_TYPE_CONTENT_TYPE = new HashMap<>();

    static {
        MEDIA_TYPE_CONTENT_TYPE.put("jpg", "image/jpeg");
        MEDIA_TYPE_CONTENT_TYPE.put("jpeg", "image/jpeg");
        MEDIA_TYPE_CONTENT_TYPE.put("png", "image/png");
        MEDIA_TYPE_CONTENT_TYPE.put("gif", "image/gif");
        MEDIA_TYPE_CONTENT_TYPE.put("mp4", "video/mp4");
        MEDIA_TYPE_CONTENT_TYPE.put("mov", "video/quicktime");
        MEDIA_TYPE_CONTENT_TYPE.put("avi", "video/x-msvideo");
        MEDIA_TYPE_CONTENT_TYPE.put("webm", "video/webm");
    }

    public String uploadFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new BadRequestException("File is empty");
        }

        String originalFilename = file.getOriginalFilename();
        String contentType = file.getContentType();
        
        // Validate file based on extension or content type
        if (!isValidFile(originalFilename, contentType)) {
            throw new BadRequestException("Invalid file type. Allowed types: jpg, jpeg, png, gif, mp4, mov, avi, webm");
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new BadRequestException("File size exceeds maximum allowed size of 50MB");
        }

        try {
            // Generate unique filename with UUID
            String fileExtension = getFileExtension(originalFilename);
            if (fileExtension.isEmpty()) {
                fileExtension = getExtensionFromContentType(contentType);
            }
            String filename = UUID.randomUUID() + "." + fileExtension;
            String resolvedContentType = getContentType(filename);

            // Upload to MinIO
            try (InputStream inputStream = file.getInputStream()) {
                minioClient.putObject(
                        PutObjectArgs.builder()
                                .bucket(bucketName)
                                .object(filename)
                                .stream(inputStream, file.getSize(), -1)
                                .contentType(resolvedContentType)
                                .build()
                );
            }

            return filename;
        } catch (MinioException e) {
            throw new BadRequestException("Failed to upload file to MinIO: " + e.getMessage());
        } catch (Exception e) {
            throw new BadRequestException("Failed to upload file: " + e.getMessage());
        }
    }


    public byte[] downloadFile(String filename) {
        try {
            if (!fileExists(filename)) {
                throw new BadRequestException("File not found: " + filename);
            }

            try (InputStream inputStream = minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(bucketName)
                            .object(filename)
                            .build()
            )) {
                return inputStream.readAllBytes();
            }
        } catch (BadRequestException e) {
            throw e;
        } catch (MinioException e) {
            throw new BadRequestException("Failed to download file from MinIO: " + e.getMessage());
        } catch (Exception e) {
            throw new BadRequestException("Failed to read file: " + e.getMessage());
        }
    }


    public void deleteFile(String filename) {
        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(bucketName)
                            .object(filename)
                            .build()
            );
        } catch (Exception e) {
            // Log error but don't throw - file might already be deleted or other issues
            System.err.println("Failed to delete file from MinIO: " + e.getMessage());
        }
    }


    public boolean fileExists(String filename) {
        try {
            minioClient.statObject(
                    StatObjectArgs.builder()
                            .bucket(bucketName)
                            .object(filename)
                            .build()
            );
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    public long getFileSize(String filename) {
        try {
            if (!fileExists(filename)) {
                throw new BadRequestException("File not found: " + filename);
            }
            return minioClient.statObject(
                    StatObjectArgs.builder()
                            .bucket(bucketName)
                            .object(filename)
                            .build()
            ).size();
        } catch (BadRequestException e) {
            throw e;
        } catch (Exception e) {
            throw new BadRequestException("Failed to get file size: " + e.getMessage());
        }
    }


    public String getContentType(String filename) {
        String extension = getFileExtension(filename);
        return MEDIA_TYPE_CONTENT_TYPE.getOrDefault(extension.toLowerCase(), "application/octet-stream");
    }


    public String generatePresignedUrl(String filename, int expirySeconds) {
        try {
            if (!fileExists(filename)) {
                throw new BadRequestException("File not found: " + filename);
            }
            return minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                            .bucket(bucketName)
                            .object(filename)
                            .expiry(expirySeconds)
                            .build()
            );
        } catch (BadRequestException e) {
            throw e;
        } catch (Exception e) {
            throw new BadRequestException("Failed to generate presigned URL: " + e.getMessage());
        }
    }


    private boolean isValidFile(String filename, String contentType) {
        if (filename != null && !filename.isEmpty()) {
            String extension = getFileExtension(filename);
            if (!extension.isEmpty() && ALLOWED_EXTENSIONS.contains(extension.toLowerCase())) {
                return true;
            }
        }
        
        if (contentType != null && !contentType.isEmpty()) {
            String lowerContentType = contentType.toLowerCase();
            return lowerContentType.startsWith("image/") || lowerContentType.startsWith("video/");
        }
        
        return false;
    }

    private String getExtensionFromContentType(String contentType) {
        if (contentType == null || contentType.isEmpty()) {
            return "jpg"; // default
        }
        
        String lowerType = contentType.toLowerCase();
        if (lowerType.contains("jpeg")) return "jpg";
        if (lowerType.contains("png")) return "png";
        if (lowerType.contains("gif")) return "gif";
        if (lowerType.contains("webp")) return "jpg"; // fallback
        if (lowerType.contains("mp4")) return "mp4";
        if (lowerType.contains("quicktime")) return "mov";
        if (lowerType.contains("msvideo")) return "avi";
        if (lowerType.contains("webm")) return "webm";
        if (lowerType.startsWith("image/")) return "jpg";
        if (lowerType.startsWith("video/")) return "mp4";
        
        return "jpg";
    }

    private String getFileExtension(String filename) {
        if (filename == null) return "";
        int lastIndex = filename.lastIndexOf('.');
        if (lastIndex > 0) {
            return filename.substring(lastIndex + 1);
        }
        return "";
    }
}
