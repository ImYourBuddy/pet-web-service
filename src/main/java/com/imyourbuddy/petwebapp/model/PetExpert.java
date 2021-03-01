package com.imyourbuddy.petwebapp.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "pet_expert")
@Data
public class PetExpert {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "first_name")
    private String firsName;

    @Column(name = "last_name")
    private String lastName;

    private String qualification;

    private String login;

    private String password;

    @Column(name = "online_help")
    private boolean onlineHelp;
}
