package com.taskit.rpadservice.Infrastucture.Outbound.Storage;

import com.taskit.rpadservice.Application.Ports.Outbound.FileStoragePort;
import com.taskit.rpadservice.Domain.Model.StoredFile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Response;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Object;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

import java.io.InputStream;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Component
public class S3FileStorageAdapter implements FileStoragePort {

    private final S3Client s3Client;
    private final S3Presigner s3Presigner;
    private final String bucketName;

    public S3FileStorageAdapter(
            S3Client s3Client,
            @Value("${cloud.aws.s3.bucket-name:taskit-files}") String bucketName) {
        this.s3Client = s3Client;
        this.s3Presigner = S3Presigner.builder()
                .region(s3Client.serviceClientConfiguration().region())
                .credentialsProvider(s3Client.serviceClientConfiguration().credentialsProvider())
                .build();
        this.bucketName = bucketName;
    }

    @Override
    public String uploadFile(String fileName, String contentType, byte[] data) {
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .contentType(contentType)
                .build();

        s3Client.putObject(putObjectRequest, RequestBody.fromBytes(data));
        return getFileUrl(fileName, 60);
    }

    @Override
    public String uploadFile(String fileName, String contentType, long contentLength, InputStream inputStream) {
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .contentType(contentType)
                .build();

        s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(inputStream, contentLength));
        return getFileUrl(fileName, 60);
    }

    @Override
    public String getFileUrl(String fileName, int expirationMinutes) {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .build();

        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(expirationMinutes))
                .getObjectRequest(getObjectRequest)
                .build();

        return s3Presigner.presignGetObject(presignRequest).url().toExternalForm();
    }

    @Override
    public List<StoredFile> getFilesByTaskId(String taskId) {
        if (taskId == null || taskId.isBlank()) {
            return Collections.emptyList();
        }

        List<StoredFile> files = new ArrayList<>();
        Set<String> prefixes = new LinkedHashSet<>(List.of("tasks/" + taskId + "/", taskId + "/"));

        for (String prefix : prefixes) {
            ListObjectsV2Request listRequest = ListObjectsV2Request.builder()
                    .bucket(bucketName)
                    .prefix(prefix)
                    .build();

            ListObjectsV2Response listResponse = s3Client.listObjectsV2(listRequest);
            for (S3Object s3Object : listResponse.contents()) {
                String key = s3Object.key();
                if (key.endsWith("/")) {
                    continue;
                }
                String rawName = key.contains("/") ? key.substring(key.lastIndexOf('/') + 1) : key;
                String originalName = rawName.replaceFirst("^[a-fA-F0-9\\-]{36}-", "");
                String fileUrl = getFileUrl(key, 60);

                files.add(new StoredFile(
                        taskId,
                        originalName,
                        key,
                        fileUrl,
                        s3Object.size(),
                        s3Object.lastModified()
                ));
            }
        }

        return files;
    }
}
