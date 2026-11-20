package com.taskit.rpadservice.Application.Ports.Inbound;

public interface SetReportStatusUseCase {
    void setReportStatus(String reportId, String status);
}
