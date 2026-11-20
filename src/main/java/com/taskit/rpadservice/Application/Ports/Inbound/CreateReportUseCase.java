package com.taskit.rpadservice.Application.Ports.Inbound;

import com.taskit.rpadservice.Domain.Model.Report;

import java.util.List;

public interface CreateReportUseCase {
    Report createReport(String taskId, String reason, List<String> users, String createdBy);
}
