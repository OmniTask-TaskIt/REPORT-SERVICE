package com.taskit.rpadservice.Application.Ports.Inbound;

import com.taskit.rpadservice.Domain.Model.Report;

import java.util.List;

public interface GetReportsByUserIdUseCase {
    List<Report> getReportsByUserId(String userId);
}
