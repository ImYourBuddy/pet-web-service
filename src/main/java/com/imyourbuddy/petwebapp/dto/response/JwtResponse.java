package com.imyourbuddy.petwebapp.dto.response;

import lombok.Data;

@Data
public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private Long id;
    private String username;
    private String firstName;
    private String lastName;

    public JwtResponse(String token,
                       Long id,
                       String username,
                       String firstName,
                       String lastName) {
        this.token = token;
        this.id = id;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
