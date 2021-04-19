package com.imyourbuddy.petwebapp.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@Table
@Data
public class Pet {

    private enum Species {
        DOG, CAT, FISH, HAMSTER, MICE, BIRD, HORSE, LIZARD, RABBIT, COW;
    }

    private enum Gender {
        MALE, FEMALE;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Enumerated(EnumType.STRING)
    private Species species;

    @NotBlank
    @Size(min = 2, max = 50)
    private String name;

    @NotBlank
    @Size(min = 5, max = 30)
    private String breed;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private Date birthdate;

    @Min(value = 1)
    private long owner;
}
