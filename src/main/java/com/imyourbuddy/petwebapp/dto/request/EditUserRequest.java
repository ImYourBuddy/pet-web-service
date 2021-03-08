package com.imyourbuddy.petwebapp.dto.request;

import lombok.Data;

@Data
public class EditUserRequest {
    private String firstName;
    private String lastName;
    private String password;
}
