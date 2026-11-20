package com.taskit.rpadservice.Application.Ports.Inbound;

public interface DeleteReportUseCase {
    void deleteReport(String reportId, String userId);
}
