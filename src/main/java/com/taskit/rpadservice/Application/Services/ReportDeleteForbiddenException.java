package com.taskit.rpadservice.Application.Services;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class ReportDeleteForbiddenException extends RuntimeException {
    public ReportDeleteForbiddenException() {
        super("Only the user who created the report can delete it");
    }
}
