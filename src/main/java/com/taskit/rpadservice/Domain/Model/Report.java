package com.taskit.rpadservice.Domain.Model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "reports")
public class Report {
    @Id
    private String reportID;
    @Field("TaskID")
    private String taskId;
    private LocalDateTime timestamp;
    private String reason;
    private String Status;
    private String winnerID;
    private List<String> users;

    public Report() {}

    public Report(String reportID, String taskId, LocalDateTime timestamp, String reason, String winnerID, List<String> users) {
        this.reportID = reportID;
        this.taskId = taskId;
        this.timestamp = timestamp;
        this.reason = reason;
        this.winnerID = winnerID;
        this.users = users;
    }

    public String getReportID() { return reportID; }
    public void setReportID(String reportID) { this.reportID = reportID; }
    public String getTaskId() { return taskId; }
    public void setTaskId(String taskId) { this.taskId = taskId; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    public String getWinnerID() { return winnerID; }
    public void setWinnerID(String winnerID) { this.winnerID = winnerID; }
    public List<String> getUsers() { return users; }
    public void setUsers(List<String> users) { this.users = users; }
    public String getStatus() { return Status; }
    public void setStatus(String status) { this.Status = status; }
}
