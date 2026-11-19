package com.taskit.rpadservice.Application.Ports.Outbound;

import com.taskit.rpadservice.Domain.Model.StoredFile;

import java.io.InputStream;
import java.util.List;

public interface FileStoragePort {
    String uploadFile(String fileName, String contentType, byte[] data);
    String uploadFile(String fileName, String contentType, long contentLength, InputStream inputStream);
    String getFileUrl(String fileName, int expirationMinutes);
    List<StoredFile> getFilesByTaskId(String taskId);
}
