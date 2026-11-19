package com.taskit.rpadservice.Infrastucture.Inbound;

import com.taskit.rpadservice.Application.Ports.Inbound.UploadFileUseCase;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/files")
public class FileController {

    private final UploadFileUseCase uploadFileUseCase;

    public FileController(UploadFileUseCase uploadFileUseCase) {
        this.uploadFileUseCase = uploadFileUseCase;
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, String>> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "folder", required = false) String folder) {
        String fileUrl = folder != null && !folder.isBlank()
                ? uploadFileUseCase.uploadFile(file, folder)
                : uploadFileUseCase.uploadFile(file);

        return ResponseEntity.ok(Map.of("url", fileUrl));
    }
}
