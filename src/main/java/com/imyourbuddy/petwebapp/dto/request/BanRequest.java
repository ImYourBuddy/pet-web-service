package com.imyourbuddy.petwebapp.dto.request;

import lombok.Data;

@Data
public class BanRequest {
    private long id;
    private String description;
    private boolean isBanned;
}
