package com.taskit.rpadservice.Application.Ports.Outbound;

import java.io.InputStream;

public interface FileStoragePort {
    String uploadFile(String fileName, String contentType, byte[] data);
    String uploadFile(String fileName, String contentType, long contentLength, InputStream inputStream);
}
