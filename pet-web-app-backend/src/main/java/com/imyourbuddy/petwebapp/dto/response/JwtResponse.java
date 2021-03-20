package com.imyourbuddy.petwebapp.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private List<String> roles;

    public JwtResponse(String token,
                       Long id,
                       String username,
                       String firstName,
                       String lastName,
                       List<String> roles) {
        this.token = token;
        this.id = id;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.roles = roles;
    }
}
