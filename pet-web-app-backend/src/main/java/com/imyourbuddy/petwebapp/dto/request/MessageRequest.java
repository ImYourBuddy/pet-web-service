package com.imyourbuddy.petwebapp.dto.request;

import lombok.Data;

@Data
public class MessageRequest {
    private long to;
    private String message;
}
