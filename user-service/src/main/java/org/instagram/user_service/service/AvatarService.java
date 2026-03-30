package org.instagram.user_service.service;

import io.minio.*;
import io.minio.errors.ErrorResponseException;
import jakarta.annotation.PostConstruct;
import org.instagram.user_service.entity.User;
import org.instagram.user_service.exception.UserNotFoundException;
import org.instagram.user_service.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.InputStream;

@Service
public class AvatarService {

    private static final Logger logger = LoggerFactory.getLogger(AvatarService.class);

    private final MinioClient minioClient;
    private final UserRepository userRepository;

    @Value("${minio.bucket:avatars}")
    private String bucket;

    @Value("${minio.public-url:http://localhost:9000}")
    private String minioPublicUrl;

    @Value("${minio.default-avatar-url}")
    private String defaultAvatarUrl;

    public AvatarService(MinioClient minioClient, UserRepository userRepository) {
        this.minioClient = minioClient;
        this.userRepository = userRepository;
    }

    // Called on app startup — ensures bucket exists and default avatar is uploaded
    @PostConstruct
    public void init() {
        try {
            boolean exists = minioClient.bucketExists(
                    BucketExistsArgs.builder().bucket(bucket).build());
            if (!exists) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucket).build());
                logger.info("Created MinIO bucket: {}", bucket);
            }
            uploadDefaultAvatarIfMissing();
            setBucketPolicy();
        } catch (Exception e) {
            logger.error("Failed to initialize avatar bucket", e);
        }
    }

    private void setBucketPolicy() {
        try {
            String policy = "{\n" +
                    "  \"Version\": \"2012-10-17\",\n" +
                    "  \"Statement\": [\n" +
                    "    {\n" +
                    "      \"Effect\": \"Allow\",\n" +
                    "      \"Principal\": \"*\",\n" +
                    "      \"Action\": [\"s3:GetObject\"],\n" +
                    "      \"Resource\": \"arn:aws:s3:::" + bucket + "/*\"\n" +
                    "    }\n" +
                    "  ]\n" +
                    "}";
            minioClient.setBucketPolicy(SetBucketPolicyArgs.builder()
                    .bucket(bucket)
                    .config(policy)
                    .build());
            logger.info("Set public read policy on bucket: {}", bucket);
        } catch (Exception e) {
            logger.warn("Could not set bucket policy (may already exist): {}", e.getMessage());
        }
    }

    private void uploadDefaultAvatarIfMissing() {
        try {
            minioClient.statObject(StatObjectArgs.builder()
                    .bucket(bucket)
                    .object("default-avatar.png")
                    .build());
            logger.info("Default avatar already exists in MinIO");
        } catch (ErrorResponseException e) {
            // Object doesn't exist — upload the default
            try (InputStream defaultAvatar = getClass()
                    .getResourceAsStream("/static/default-avatar.png")) {
                if (defaultAvatar == null) {
                    logger.warn("default-avatar.png not found in resources");
                    return;
                }
                minioClient.putObject(PutObjectArgs.builder()
                        .bucket(bucket)
                        .object("default-avatar.png")
                        .stream(defaultAvatar, -1, 10485760)
                        .contentType("image/png")
                        .build());
                logger.info("Uploaded default avatar to MinIO");
            } catch (Exception ex) {
                logger.error("Failed to upload default avatar", ex);
            }
        } catch (Exception e) {
            logger.error("Failed to check default avatar", e);
        }
    }

    public String uploadAvatar(Long userId, MultipartFile file) {
        validateFile(file);

        try {
            String extension = getExtension(file.getOriginalFilename());
            String objectName = "user-" + userId + "-" + System.currentTimeMillis() + extension;

            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(bucket)
                    .object(objectName)
                    .stream(file.getInputStream(), file.getSize(), -1)
                    .contentType(file.getContentType())
                    .build());

            String url = buildUrl(objectName);
            logger.info("Uploaded avatar for userId={} url={}", userId, url);

            // Save URL to user profile
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new UserNotFoundException("User not found"));

            // Delete old avatar if it's not the default
            deleteOldAvatar(user.getProfilePictureUrl());

            user.setProfilePictureUrl(url);
            userRepository.save(user);

            return url;

        } catch (UserNotFoundException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Failed to upload avatar for userId={}", userId, e);
            throw new RuntimeException("Failed to upload avatar", e);
        }
    }

    public String deleteAvatar(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        deleteOldAvatar(user.getProfilePictureUrl());
        user.setProfilePictureUrl(defaultAvatarUrl);
        userRepository.save(user);

        return defaultAvatarUrl;
    }

    private void deleteOldAvatar(String currentUrl) {
        if (currentUrl == null || currentUrl.equals(defaultAvatarUrl)) return;
        try {
            String objectName = extractObjectName(currentUrl);
            minioClient.removeObject(RemoveObjectArgs.builder()
                    .bucket(bucket)
                    .object(objectName)
                    .build());
            logger.info("Deleted old avatar: {}", objectName);
        } catch (Exception e) {
            logger.warn("Could not delete old avatar: {}", currentUrl);
        }
    }

    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File cannot be empty");
        }
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("Only image files are allowed");
        }
        if (file.getSize() > 5 * 1024 * 1024) {
            throw new IllegalArgumentException("File size cannot exceed 5MB");
        }
    }

    private String getExtension(String filename) {
        if (filename == null || !filename.contains(".")) return ".jpg";
        return filename.substring(filename.lastIndexOf("."));
    }

    private String buildUrl(String objectName) {
        return minioPublicUrl + "/" + bucket + "/" + objectName;
    }

    private String extractObjectName(String url) {
        return url.substring(url.lastIndexOf("/") + 1);
    }

    public String getDefaultAvatarUrl() {
        return defaultAvatarUrl;
    }
}