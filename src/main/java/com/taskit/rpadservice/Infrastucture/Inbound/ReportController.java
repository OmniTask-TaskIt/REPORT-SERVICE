package com.taskit.rpadservice.Infrastucture.Inbound;

import com.taskit.rpadservice.Application.Services.ReportService;
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

    @PostMapping
    public ResponseEntity<Report> createReport(
            @RequestParam String taskId,
            @RequestParam String reason,
            @RequestParam List<String> users) {
        return ResponseEntity.ok(reportService.createReport(taskId, reason, users));
    }

    @GetMapping("/{reportID}/reason")
    public ResponseEntity<String> getReportReason(@PathVariable String reportID) {
        String reason = reportService.getReportReason(reportID);
        return reason != null ? ResponseEntity.ok(reason) : ResponseEntity.notFound().build();
    }

    @PostMapping("/{reportID}/winner")
    public ResponseEntity<Void> defineReportWinner(@PathVariable String reportID, @RequestParam String winnerID) {
        reportService.defineReportWinner(reportID, winnerID);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{reportID}/messages")
    public ResponseEntity<List<Message>> getReportMessages(@PathVariable String reportID) {
        return ResponseEntity.ok(reportService.getReportMessages(reportID));
    }
}
