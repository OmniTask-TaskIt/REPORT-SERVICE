package com.taskit.rpadservice.Domain.Repository;

import com.taskit.rpadservice.Domain.Model.Report;

import java.util.List;
import java.util.Optional;

public interface ReportRepository {
    Report save(Report report);
    Optional<Report> findById(String reportID);
    List<Report> findAll();
    List<Report> findAllByStatus(String status);
    List<Report> findAllByUserId(String userId);

}
