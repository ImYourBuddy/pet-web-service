package com.imyourbuddy.petwebapp.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Entity
@Table
@Data
@NoArgsConstructor
public class Ban {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "user_id")
    @Min(value = 1)
    private long userId;

    @NotBlank
    private String description;

    public Ban(@Min(value = 1) long userId, @NotBlank String description) {
        this.userId = userId;
        this.description = description;
    }
}
