package com.taskit.rpadservice.Infrastucture.Outbound.Persistence;

import com.taskit.rpadservice.Domain.Model.Report;
import com.taskit.rpadservice.Domain.Repository.ReportRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ReportRepositoryAdapter implements ReportRepository {
    private final ReportRepositoryMongo repository;

    public ReportRepositoryAdapter(ReportRepositoryMongo repository) {
        this.repository = repository;
    }

    @Override
    public Report save(Report report) {
        return repository.save(report);
    }

    @Override
    public Optional<Report> findById(String reportID) {
        return repository.findById(reportID);
    }

    @Override
    public List<Report> findAll() {
        return repository.findAll();
    }

    @Override
    public List<Report> findAllByStatus(String status) {
        return repository.findAll().stream()
                .filter(report -> status.equals(report.getStatus()))
                .toList();
    }

    @Override
    public List<Report> findAllByUserId(String userId) {
        return repository.findAll().stream()
                .filter(report -> report.getUsers() != null && report.getUsers().contains(userId))
                .toList();
    }

    @Override
    public void deleteById(String reportID) {
        repository.deleteById(reportID);
    }
}
