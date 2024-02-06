package com.GP.ELsayes.exception;

import lombok.Data;

import java.time.LocalDateTime;

@Data

public class ErrorResponse {


    private String message;
    private LocalDateTime timestamp;

    public ErrorResponse(String message) {
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

}
