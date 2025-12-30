package com.sushant.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

//@Data
//@NoArgsConstructor
//@AllArgsConstructor
public class ErrorDetails {
//    error details
    private  String error;
    private  String details;
    private LocalDateTime timestamp;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public ErrorDetails(String error, String details, LocalDateTime timestamp) {
        this.error = error;
        this.details = details;
        this.timestamp = timestamp;
    }

    public ErrorDetails() {

    }
}
