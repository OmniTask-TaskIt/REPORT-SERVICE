package com.taskit.rpadservice.Application.Ports.Inbound;

import com.taskit.rpadservice.Domain.Model.Message;
import java.util.List;

public interface GetReportMessagesUseCase {
    List<Message> getReportMessages(String reportID);
}
