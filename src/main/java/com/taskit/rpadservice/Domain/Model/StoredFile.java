package com.taskit.rpadservice.Domain.Model;

import java.time.Instant;

public class StoredFile {
    private String taskId;
    private String fileName;
    private String key;
    private String url;
    private Long size;
    private Instant lastModified;

    public StoredFile() {}

    public StoredFile(String taskId, String fileName, String key, String url, Long size, Instant lastModified) {
        this.taskId = taskId;
        this.fileName = fileName;
        this.key = key;
        this.url = url;
        this.size = size;
        this.lastModified = lastModified;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public Instant getLastModified() {
        return lastModified;
    }

    public void setLastModified(Instant lastModified) {
        this.lastModified = lastModified;
    }
}
