package org.instagram.postservice.service;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class MinioBucketInitializer {

    @Autowired
    private MinioClient minioClient;

    @Value("${minio.bucket-name}")
    private String bucketName;

    /**
     * Initialize MinIO bucket on application startup
     * Creates the bucket if it doesn't exist
     */
    @EventListener(ApplicationReadyEvent.class)
    public void initializeBucket() {
        try {
            boolean bucketExists = minioClient.bucketExists(
                    BucketExistsArgs.builder().bucket(bucketName).build()
            );

            if (!bucketExists) {
                minioClient.makeBucket(
                        MakeBucketArgs.builder().bucket(bucketName).build()
                );
                System.out.println("✓ MinIO bucket '" + bucketName + "' created successfully");
            } else {
                System.out.println("✓ MinIO bucket '" + bucketName + "' already exists");
            }
        } catch (Exception e) {
            System.err.println("⚠ Failed to initialize MinIO bucket: " + e.getMessage());
            throw new RuntimeException("Failed to initialize MinIO bucket", e);
        }
    }
}
