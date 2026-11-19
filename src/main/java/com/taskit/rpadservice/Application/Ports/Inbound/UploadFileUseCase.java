package com.taskit.rpadservice.Application.Ports.Inbound;

import org.springframework.web.multipart.MultipartFile;

public interface UploadFileUseCase {
    String uploadFile(MultipartFile file);
    String uploadFile(MultipartFile file, String folder);
}
