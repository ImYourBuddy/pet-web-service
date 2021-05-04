package com.imyourbuddy.petwebapp.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeletePostRequest {
    private boolean delete;
}
