package com.imyourbuddy.petwebapp.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Access;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EditUserRequest {
    @NotBlank
    @Size(min = 2, max = 30)
    private String firstName;
    @NotBlank
    @Size(min = 2, max = 30)
    private String lastName;
}
