package com.taskit.rpadservice.Application.Ports.Inbound;

import com.taskit.rpadservice.Domain.Model.StoredFile;

import java.util.List;

public interface GetFilesByTaskIdUseCase {
    List<StoredFile> getFilesByTaskId(String taskId);
}
