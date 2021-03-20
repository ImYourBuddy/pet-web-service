package com.imyourbuddy.petwebapp.dto.request;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Data
public class BanRequest {
    @NotBlank
    @Min(value = 1)
    private long userId;
    @NotBlank
    private String description;
    private boolean isBanned;
}
