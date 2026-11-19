package com.taskit.rpadservice.Infrastucture.Inbound;

import com.taskit.rpadservice.Application.Ports.Inbound.GetFilesByTaskIdUseCase;
import com.taskit.rpadservice.Application.Ports.Inbound.UploadFileUseCase;
import com.taskit.rpadservice.Application.Ports.Outbound.FileStoragePort;
import com.taskit.rpadservice.Domain.Model.StoredFile;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/files")
public class FileController {

    private final UploadFileUseCase uploadFileUseCase;
    private final GetFilesByTaskIdUseCase getFilesByTaskIdUseCase;
    private final FileStoragePort fileStoragePort;

    public FileController(
            UploadFileUseCase uploadFileUseCase,
            GetFilesByTaskIdUseCase getFilesByTaskIdUseCase,
            FileStoragePort fileStoragePort) {
        this.uploadFileUseCase = uploadFileUseCase;
        this.getFilesByTaskIdUseCase = getFilesByTaskIdUseCase;
        this.fileStoragePort = fileStoragePort;
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, String>> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "taskId", required = false) String taskId,
            @RequestParam(value = "folder", required = false) String folder) {
        String fileUrl = uploadFileUseCase.uploadFile(file, taskId, folder);
        return ResponseEntity.ok(Map.of("url", fileUrl));
    }

    @GetMapping("/task/{taskId}")
    public ResponseEntity<List<StoredFile>> getFilesByTaskIdPath(@PathVariable("taskId") String taskId) {
        if (taskId == null || taskId.isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(getFilesByTaskIdUseCase.getFilesByTaskId(taskId));
    }

    @GetMapping
    public ResponseEntity<List<StoredFile>> getFilesByTaskIdParam(@RequestParam("taskId") String taskId) {
        if (taskId == null || taskId.isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(getFilesByTaskIdUseCase.getFilesByTaskId(taskId));
    }

    @GetMapping("/download")
    public ResponseEntity<Void> downloadFile(
            @RequestParam("key") String key,
            @RequestParam(value = "expirationMinutes", defaultValue = "60") int expirationMinutes) {
        if (key == null || key.isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        if (expirationMinutes < 1 || expirationMinutes > 10080) {
            return ResponseEntity.badRequest().build();
        }

        String url = fileStoragePort.getFileUrl(key, expirationMinutes);
        return ResponseEntity.status(302)
                .header("Location", url)
                .build();
    }
}
