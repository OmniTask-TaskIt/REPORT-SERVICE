package com.taskit.rpadservice.Application.Services;

import com.taskit.rpadservice.Application.Ports.Inbound.GetFilesByTaskIdUseCase;
import com.taskit.rpadservice.Application.Ports.Inbound.UploadFileUseCase;
import com.taskit.rpadservice.Application.Ports.Outbound.FileStoragePort;
import com.taskit.rpadservice.Domain.Model.StoredFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
public class FileStorageService implements UploadFileUseCase, GetFilesByTaskIdUseCase {

    private final FileStoragePort fileStoragePort;

    public FileStorageService(FileStoragePort fileStoragePort) {
        this.fileStoragePort = fileStoragePort;
    }

    @Override
    public String uploadFile(MultipartFile file) {
        return uploadFile(file, "general", "uploads");
    }

    @Override
    public String uploadFile(MultipartFile file, String taskId) {
        String folder = (taskId != null && !taskId.isBlank()) ? "tasks/" + taskId : "uploads";
        return uploadFile(file, taskId, folder);
    }

    @Override
    public String uploadFile(MultipartFile file, String taskId, String folder) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("El archivo no puede estar vacío");
        }

        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || originalFilename.isBlank()) {
            originalFilename = "file_" + System.currentTimeMillis();
        }

        String sanitizedFilename = originalFilename.replaceAll("[^a-zA-Z0-9._-]", "_");
        String prefix = "";
        if (taskId != null && !taskId.isBlank()) {
            prefix = "tasks/" + taskId + "/";
        } else if (folder != null && !folder.isBlank()) {
            prefix = folder.replaceAll("/+$", "") + "/";
        }

        String fileKey = prefix + UUID.randomUUID() + "-" + sanitizedFilename;

        String contentType = file.getContentType();
        if (contentType == null || contentType.isBlank()) {
            contentType = "application/octet-stream";
        }

        try {
            return fileStoragePort.uploadFile(fileKey, contentType, file.getSize(), file.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException("Error al leer el archivo para subir a S3", e);
        }
    }

    @Override
    public List<StoredFile> getFilesByTaskId(String taskId) {
        if (taskId == null || taskId.isBlank()) {
            return Collections.emptyList();
        }
        return fileStoragePort.getFilesByTaskId(taskId);
    }
}
