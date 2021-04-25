package com.imyourbuddy.petwebapp.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BanRequest {
    @Min(value = 1)
    private long userId;

    @NotBlank
    private String description;
}
