package com.taskit.rpadservice.Infrastucture.Inbound;

import com.taskit.rpadservice.Application.Services.ReportService;
import com.taskit.rpadservice.Application.Services.ReportNotFoundException;
import com.taskit.rpadservice.Application.Services.ReportDeleteForbiddenException;
import com.taskit.rpadservice.Domain.Model.Message;
import com.taskit.rpadservice.Domain.Model.Report;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping
    public ResponseEntity<List<Report>> getAllReports() {
        return ResponseEntity.ok(reportService.getAllReports());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Report>> getReportsByUserIdPath(@PathVariable String userId) {
        if (userId == null || userId.isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(reportService.getReportsByUserId(userId));
    }

    @PostMapping
    public ResponseEntity<Report> createReport(
            @RequestParam String taskId,
            @RequestParam String reason,
            @RequestParam List<String> users,
            @RequestParam String createdBy) {
        return ResponseEntity.ok(reportService.createReport(taskId, reason, users, createdBy));
    }

    @GetMapping("/{reportID}/reason")
    public ResponseEntity<String> getReportReason(@PathVariable String reportID) {
        String reason = reportService.getReportReason(reportID);
        return reason != null ? ResponseEntity.ok(reason) : ResponseEntity.notFound().build();
    }

    @GetMapping("/{reportID}/created-by")
    public ResponseEntity<String> getReportCreatedBy(@PathVariable String reportID) {
        if (reportID == null || reportID.isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        try {
            String createdBy = reportService.getReportCreatedBy(reportID);
            return createdBy != null
                    ? ResponseEntity.ok(createdBy)
                    : ResponseEntity.notFound().build();
        } catch (ReportNotFoundException exception) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{reportID}/winner")
    public ResponseEntity<Void> defineReportWinner(@PathVariable String reportID, @RequestParam String winnerID) {
        reportService.defineReportWinner(reportID, winnerID);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{reportID}/status")
    public ResponseEntity<Void> setReportStatus(
            @PathVariable String reportID,
            @RequestParam String status) {
        if (reportID == null || reportID.isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        if (status == null || status.isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        try {
            reportService.setReportStatus(reportID, status);
            return ResponseEntity.ok().build();
        } catch (ReportNotFoundException exception) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{reportID}/messages")
    public ResponseEntity<List<Message>> getReportMessages(@PathVariable String reportID) {
        return ResponseEntity.ok(reportService.getReportMessages(reportID));
    }

    @DeleteMapping("/{reportID}")
    public ResponseEntity<Void> deleteReport(
            @PathVariable String reportID,
            @RequestParam String userId) {
        if (reportID == null || reportID.isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        if (userId == null || userId.isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        try {
            reportService.deleteReport(reportID, userId);
            return ResponseEntity.noContent().build();
        } catch (ReportNotFoundException exception) {
            return ResponseEntity.notFound().build();
        } catch (ReportDeleteForbiddenException exception) {
            return ResponseEntity.status(403).build();
        }
    }
}
