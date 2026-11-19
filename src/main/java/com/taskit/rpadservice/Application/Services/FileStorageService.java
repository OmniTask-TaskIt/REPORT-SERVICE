package com.taskit.rpadservice.Application.Services;

import com.taskit.rpadservice.Application.Ports.Inbound.UploadFileUseCase;
import com.taskit.rpadservice.Application.Ports.Outbound.FileStoragePort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class FileStorageService implements UploadFileUseCase {

    private final FileStoragePort fileStoragePort;

    public FileStorageService(FileStoragePort fileStoragePort) {
        this.fileStoragePort = fileStoragePort;
    }

    @Override
    public String uploadFile(MultipartFile file) {
        return uploadFile(file, "uploads");
    }

    @Override
    public String uploadFile(MultipartFile file, String folder) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("El archivo no puede estar vacío");
        }

        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || originalFilename.isBlank()) {
            originalFilename = "file_" + System.currentTimeMillis();
        }

        String sanitizedFilename = originalFilename.replaceAll("[^a-zA-Z0-9._-]", "_");
        String fileKey = (folder != null && !folder.isBlank() ? folder.replaceAll("/+$", "") + "/" : "")
                + UUID.randomUUID() + "-" + sanitizedFilename;

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
}
