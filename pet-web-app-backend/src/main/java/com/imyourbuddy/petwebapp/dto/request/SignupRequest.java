package com.imyourbuddy.petwebapp.dto.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;


@Data
public class SignupRequest {
    @NotBlank
    @Size(min = 3, max = 50)
    private String username;

    @NotBlank
    @Size(min = 5, max = 100)
    private String password;

    @NotBlank
    @Size(min = 2, max = 30)
    private String firstName;

    @NotBlank
    @Size(min = 2, max = 30)
    private String lastName;
}
