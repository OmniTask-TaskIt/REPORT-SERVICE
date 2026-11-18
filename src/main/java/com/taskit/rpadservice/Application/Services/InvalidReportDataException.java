package com.taskit.rpadservice.Application.Services;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidReportDataException extends RuntimeException {
    public InvalidReportDataException(String message) {
        super(message);
    }
}
