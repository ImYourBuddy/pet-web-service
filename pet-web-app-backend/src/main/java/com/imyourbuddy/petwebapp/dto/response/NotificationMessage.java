package com.imyourbuddy.petwebapp.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationMessage {
    private long chatId;

    private long sender;

    private String senderName;

    private String text;

    private Date timestamp;
}
