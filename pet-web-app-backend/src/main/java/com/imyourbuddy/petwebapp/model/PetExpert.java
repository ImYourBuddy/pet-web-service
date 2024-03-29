package com.imyourbuddy.petwebapp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(name = "pet_expert")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PetExpert{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank
    @Size(min = 20, max = 255)
    private String qualification;

    @Column(name = "online_help")
    private boolean onlineHelp;

    @Column(name = "user_id")
    @Min(value = 1)
    private long userId;

    private long reputation;

    private boolean confirmed;
}
