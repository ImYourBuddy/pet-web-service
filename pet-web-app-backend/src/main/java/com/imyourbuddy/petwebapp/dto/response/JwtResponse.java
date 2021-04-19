package com.imyourbuddy.petwebapp.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private Long id;
    private List<String> roles;

    public JwtResponse(String token,
                       Long id,
                       List<String> roles) {
        this.token = token;
        this.id = id;
        this.roles = roles;
    }
}
