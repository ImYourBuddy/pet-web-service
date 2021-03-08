package com.imyourbuddy.petwebapp.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "pet_expert")
@Data
public class PetExpert{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String qualification;

    @Column(name = "online_help")
    private boolean onlineHelp;

    @Column(name = "user_id")
    private long userId;
}
