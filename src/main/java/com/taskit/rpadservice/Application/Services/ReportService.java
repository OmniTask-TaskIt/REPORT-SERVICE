package com.taskit.rpadservice.Application.Services;

import com.taskit.rpadservice.Application.Ports.Inbound.*;
import com.taskit.rpadservice.Domain.Model.*;
import com.taskit.rpadservice.Domain.Repository.ReportRepository;
import com.taskit.rpadservice.Infrastucture.Outbound.Messaging.MessageClient;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ReportService implements CreateReportUseCase, GetReportReasonUseCase, DefineReportWinnerUseCase, GetReportMessagesUseCase {

    private final ReportRepository reportRepository;
    private final MessageClient chatClient;

    public ReportService(ReportRepository reportRepository, MessageClient chatClient) {
        this.reportRepository = reportRepository;
        this.chatClient = chatClient;
    }

    @Override
    public Report createReport(String taskId, String reason, List<String> users) {
        if (users == null || users.size() != 2 || users.stream().anyMatch(user -> user == null || user.isBlank())) {
            throw new InvalidReportDataException("users must contain the two participant IDs");
        }
        Report report = new Report(UUID.randomUUID().toString(), taskId, LocalDateTime.now(), reason, null, List.copyOf(users));
        report.setStatus("OPEN");
        return reportRepository.save(report);
    }

    @Override
    public String getReportReason(String reportID) {
        return reportRepository.findById(reportID).map(Report::getReason).orElse(null);
    }

    @Override
    public void defineReportWinner(String reportID, String winnerID) {
        Report report = reportRepository.findById(reportID)
                .orElseThrow(() -> new ReportNotFoundException(reportID));
        report.setWinnerID(winnerID);
        report.setStatus("CLOSED");
        reportRepository.save(report);
    }

    @Override
    public List<Message> getReportMessages(String reportID) {
        return reportRepository.findById(reportID)
                .map(report -> chatClient.getMessages(requireTaskId(report)).stream()
                        .map(dto -> new Message(dto.getMessageId(), dto.getChatId(), dto.getSenderId(), dto.getContent(), dto.getTimestamp()))
                        .collect(Collectors.toList()))
                .orElseThrow(() -> new ReportNotFoundException(reportID));
    }

    private String requireTaskId(Report report) {
        if (report.getTaskId() == null || report.getTaskId().isBlank()) {
            throw new IllegalStateException("Report " + report.getReportID() + " has no taskId");
        }
        return report.getTaskId();
    }
}
