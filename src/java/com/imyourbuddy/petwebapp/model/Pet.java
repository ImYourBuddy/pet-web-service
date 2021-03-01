package com.imyourbuddy.petwebapp.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table
@Data
public class Pet {

    private enum Species {
        DOG, CAT, FISH;
    }

    private enum Gender {
        MALE, FEMALE;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Enumerated(EnumType.STRING)
    private Species species;

    private String name;

    private String breed;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private Date birthdate;

    private long owner;
}
